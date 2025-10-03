package com.phegon.phegonbank.auth_users.services.impl;

import com.phegon.phegonbank.auth_users.dtos.UpdatePasswordRequest;
import com.phegon.phegonbank.auth_users.dtos.UserDTO;
import com.phegon.phegonbank.auth_users.entity.User;
import com.phegon.phegonbank.auth_users.repo.UserRepo;
import com.phegon.phegonbank.auth_users.services.UserService;
import com.phegon.phegonbank.aws.S3Service;
import com.phegon.phegonbank.exceptions.BadRequestException;
import com.phegon.phegonbank.exceptions.NotFoundException;
import com.phegon.phegonbank.notification.dtos.NotificationDTO;
import com.phegon.phegonbank.notification.services.NotificationService;
import com.phegon.phegonbank.res.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {


    private final UserRepo userRepo;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    private final S3Service s3Service;


    //this wil save images to the backend root folder
//    private final String uploadDir = "uploads/profile-pictures/";

    //this wil save images to the frontend public folder for easy access in the frontend
    private final String uploadDir = "/Users/mac/phegonDev/phegon-bank-react/public/profile-picture/";


    @Override
    public User getCurrentLoggedInUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new NotFoundException("User is not authenticated");
        }
        String email = authentication.getName();

        return userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("user not found"));
    }


    @Override
    public Response<UserDTO> getMyProfile() {

        User user = getCurrentLoggedInUser();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User retrieved")
                .data(userDTO)
                .build();
    }

    @Override
    public Response<Page<UserDTO>> getAllUsers(int page, int size) {

        Page<User> users = userRepo.findAll(PageRequest.of(page, size));

        Page<UserDTO> userDTOS = users.map(user -> modelMapper.map(user, UserDTO.class));

        return Response.<Page<UserDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Users retrieved")
                .data(userDTOS)
                .build();

    }

    @Override
    public Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = getCurrentLoggedInUser();

        String newPassword = updatePasswordRequest.getNewPassword();
        String oldPassword = updatePasswordRequest.getOldPassword();

        if (oldPassword == null || newPassword == null) {
            throw new BadRequestException("Old and New Password Required");
        }

        // Validate the old password.
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Old Password not Correct");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());

        userRepo.save(user);


        // Send password change confirmation email.
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("name", user.getFirstName());

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(user.getEmail())
                .subject("Your Password Was Successfully Changed")
                .templateName("password-change")
                .templateVariables(templateVariables)
                .build();

        notificationService.sendEmail(notificationDTO, user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password Changed Successfully")
                .build();

    }

    @Override
    public Response<?> uploadProfilePicture(MultipartFile file) {
        User user = getCurrentLoggedInUser();

        try {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            if (user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()) {
                Path oldFile = Paths.get(user.getProfilePictureUrl());
                if (Files.exists(oldFile)) {
                    Files.delete(oldFile);
                }
            }


            // Generate a unique file name to avoid conflicts
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String newFileName = UUID.randomUUID() + fileExtension;
            Path filePath = uploadPath.resolve(newFileName);

            Files.copy(file.getInputStream(), filePath);

//            String fileUrl = uploadDir + newFileName; //this is for backend
            String fileUrl =  "profile-picture/" + newFileName;// this is the relative path from the frontend


            user.setProfilePictureUrl(fileUrl);
            userRepo.save(user);

            return Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Profile picture uploaded successfully.")
                    .data(fileUrl)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public Response<?> uploadProfilePictureToS3(MultipartFile file){

        log.info("Inside uploadProfilePictureToS3()");
        User user = getCurrentLoggedInUser();

        try {

            if(user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()){
                s3Service.deleteFile(user.getProfilePictureUrl());
            }
            String s3Url = s3Service.uploadFile(file, "profile-pictures");

            log.info("profile url is: {}", s3Url );

            user.setProfilePictureUrl(s3Url);
            userRepo.save(user);

            return Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Profile picture uploaded successfully.")
                    .data(s3Url)
                    .build();

        }catch (IOException e){

            throw new RuntimeException(e.getMessage());
        }
    }
}


















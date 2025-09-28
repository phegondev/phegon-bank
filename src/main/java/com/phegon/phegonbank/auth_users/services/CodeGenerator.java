package com.phegon.phegonbank.auth_users.services;


import com.phegon.phegonbank.auth_users.repo.PasswordResetCodeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class CodeGenerator {
    private final PasswordResetCodeRepo passwordResetCodeRepo;

    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 5;


    public String generateUniqueCode(){
        String code;
        do{
            code = generateRandomCode();

        }while (passwordResetCodeRepo.findByCode(code).isPresent());

        return code;
    }


    private String generateRandomCode(){
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i <CODE_LENGTH; i++){
            int index = random.nextInt(ALPHA_NUMERIC.length());
            sb.append(ALPHA_NUMERIC.charAt(index));
        }
        return sb.toString();
    }
}













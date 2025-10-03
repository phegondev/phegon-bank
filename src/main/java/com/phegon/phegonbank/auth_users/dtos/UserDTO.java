package com.phegon.phegonbank.auth_users.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.phegon.phegonbank.account.dtos.AccountDTO;
import com.phegon.phegonbank.role.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;
    private String phoneNumber;

    private String email;

    @JsonIgnore
    private String password;

    private String profilePictureUrl;
    private boolean active;

    private List<Role> roles;

    @JsonManagedReference// if helps avoid recursion loop by ignoring the userDTO withing the AccountDTO
    private List<AccountDTO> accounts;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

package com.phegon.phegonbank.auth_users.services;

import com.phegon.phegonbank.auth_users.dtos.LoginRequest;
import com.phegon.phegonbank.auth_users.dtos.LoginResponse;
import com.phegon.phegonbank.auth_users.dtos.RegistrationRequest;
import com.phegon.phegonbank.auth_users.dtos.ResetPasswordRequest;
import com.phegon.phegonbank.res.Response;

public interface AuthService {
    Response<String > register(RegistrationRequest request);
    Response<LoginResponse> login(LoginRequest loginRequest);
    Response<? > forgetPassword(String email);
    Response<? > updatePasswordViaResetCode(ResetPasswordRequest resetPasswordRequest);
}

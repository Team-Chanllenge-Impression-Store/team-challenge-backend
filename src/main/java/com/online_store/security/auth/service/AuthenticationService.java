package com.online_store.security.auth.service;

import com.online_store.security.auth.dto.ChangePasswordRequest;
import com.online_store.security.auth.dto.LoginRequest;
import com.online_store.security.auth.dto.LoginResponse;
import com.online_store.security.auth.dto.SignupRequest;
import com.online_store.utils.MessageResponse;
import com.online_store.web.user.model.User;
import org.springframework.http.ResponseCookie;

/**
 * This interface provides methods for authentication, registration and token refreshing
 */
public interface AuthenticationService {

    LoginResponse login(LoginRequest loginRequest);

    User register(SignupRequest signupRequest);

    LoginResponse refreshToken(String token);

    ResponseCookie logout();

    MessageResponse changeUserPassword(ChangePasswordRequest request);
}

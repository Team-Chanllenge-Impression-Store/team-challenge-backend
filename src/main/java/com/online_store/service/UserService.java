package com.online_store.service;

import com.online_store.dto.request.ChangePasswordRequest;
import com.online_store.dto.request.LoginRequest;
import com.online_store.dto.request.SignupRequest;
import com.online_store.dto.response.MessageResponse;
import com.online_store.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    MessageResponse registerUser(SignupRequest request);

    MessageResponse changeUserPassword(ChangePasswordRequest request);

    User findUserByEmail(String email);

    UserDetails getUserPrincipal(LoginRequest loginRequest);
}

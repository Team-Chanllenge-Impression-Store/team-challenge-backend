package com.online_store.web.user.service;

import com.online_store.security.auth.dto.ChangePasswordRequest;
import com.online_store.security.auth.dto.SignupRequest;
import com.online_store.utils.MessageResponse;
import com.online_store.web.user.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Boolean existsByEmail(String email);

    User createUser(User user);

    MessageResponse registerUser(SignupRequest request);

    MessageResponse changeUserPassword(ChangePasswordRequest request);

    User findUserByEmail(String email);
}

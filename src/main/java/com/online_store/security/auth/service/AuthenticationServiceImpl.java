package com.online_store.security.auth.service;

import com.online_store.security.auth.dto.ChangePasswordRequest;
import com.online_store.security.auth.dto.LoginRequest;
import com.online_store.security.auth.dto.LoginResponse;
import com.online_store.security.auth.dto.SignupRequest;
import com.online_store.utils.MessageResponse;
import com.online_store.utils.constant.ErrorMessage;
import com.online_store.utils.exception.EmailAlreadyExistsException;
import com.online_store.utils.exception.JwtTokenException;
import com.online_store.web.user.model.Role;
import com.online_store.web.user.model.User;
import com.online_store.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
        );

        User user = (User) userService.loadUserByUsername(loginRequest.getEmail());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public User register(SignupRequest signupRequest) {
        if (Boolean.TRUE.equals(userService.existsByEmail(signupRequest.getEmail()))) {
            log.warn("Email already exists: {}", signupRequest.getEmail());
            throw new EmailAlreadyExistsException(ErrorMessage.EMAIL_IN_USE);
        }

        if (signupRequest.getPassword() != null && !signupRequest.getPassword().equals(signupRequest.getPassword2())) {
            log.warn("Passwords do not match");
            throw new IllegalArgumentException(ErrorMessage.PASSWORDS_DO_NOT_MATCH);
        }

        return userService.createUser(User.builder()
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .roles(Set.of(Role.USER))
                .active(true)
                .build());
    }

    @Override
    public LoginResponse refreshToken(String token) {
        if (!jwtService.isRefreshToken(token)) {
            log.warn("Invalid refresh token: {}", token);
            throw new JwtTokenException(ErrorMessage.INVALID_REFRESH_TOKEN);
        }
        String email = jwtService.extractEmailFromToken(token);
        User user = (User) userService.loadUserByUsername(email);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public ResponseCookie logout() {
        return jwtService.getCleanJwtCookie();
    }

    @Override
    public MessageResponse changeUserPassword(ChangePasswordRequest request) {
        return userService.changeUserPassword(request);
    }
}

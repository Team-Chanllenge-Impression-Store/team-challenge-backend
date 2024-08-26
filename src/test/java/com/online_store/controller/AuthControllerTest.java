package com.online_store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online_store.configuration.WebSecurityTestConfig;
import com.online_store.constants.ErrorMessage;
import com.online_store.constants.Path;
import com.online_store.constants.SuccessMessage;
import com.online_store.dto.request.ChangePasswordRequest;
import com.online_store.dto.request.LoginRequest;
import com.online_store.dto.request.SignupRequest;
import com.online_store.dto.response.MessageResponse;
import com.online_store.security.jwt.JwtUtils;
import com.online_store.security.services.UserDetailsImpl;
import com.online_store.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(WebSecurityTestConfig.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;
    private ChangePasswordRequest changePasswordRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("test@example.com", "password");
        signupRequest = new SignupRequest("test@example.com", "John", "Doe", "testPassword123", "testPassword123");
        changePasswordRequest = new ChangePasswordRequest("test@example.com", "oldPassword", "newPassword");
    }

    @Test
    void authenticateUser_Success() throws Exception {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@example.com", "password", true, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(userService.getUserPrincipal(any(LoginRequest.class))).thenReturn(userDetails);
        when(jwtUtils.generateJwtCookie(any(UserDetailsImpl.class))).thenReturn(ResponseCookie.from("jwt", "token").build());

        mockMvc.perform(post(Path.AUTH + "/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    @Test
    void authenticateUser_UserNotFound() throws Exception {
        when(userService.findUserByEmail(anyString())).thenThrow(new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND));

        mockMvc.perform(post(Path.AUTH + "/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.response").value(String.valueOf(HttpStatus.BAD_REQUEST)))
                .andExpect(jsonPath("$.message").value(ErrorMessage.USER_NOT_FOUND));
    }

    @Test
    void registerUser_Success() throws Exception {
        when(userService.registerUser(any(SignupRequest.class)))
                .thenReturn(new MessageResponse(String.valueOf(HttpStatus.OK), SuccessMessage.USER_CREATED));

        mockMvc.perform(post(Path.AUTH + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(String.valueOf(HttpStatus.OK)))
                .andExpect(jsonPath("$.message").value(SuccessMessage.USER_CREATED));
    }

    @Test
    void registerUser_Failure() throws Exception {
        when(userService.registerUser(any(SignupRequest.class)))
                .thenReturn(new MessageResponse(String.valueOf(HttpStatus.BAD_REQUEST), ErrorMessage.EMAIL_IN_USE));

        mockMvc.perform(post(Path.AUTH + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.response").value(String.valueOf(HttpStatus.BAD_REQUEST)))
                .andExpect(jsonPath("$.message").value(ErrorMessage.EMAIL_IN_USE));
    }

    @Test
    void logoutUser_Success() throws Exception {
        when(jwtUtils.getCleanJwtCookie()).thenReturn(ResponseCookie.from("jwt", "").build());

        mockMvc.perform(post(Path.AUTH + "/signout"))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(jsonPath("$.response").value(String.valueOf(HttpStatus.OK)))
                .andExpect(jsonPath("$.message").value(SuccessMessage.USER_LOGGED_OUT));
    }

    @Test
    void passwordReset_Success() throws Exception {
        when(userService.changeUserPassword(any(ChangePasswordRequest.class)))
                .thenReturn(new MessageResponse(String.valueOf(HttpStatus.OK), SuccessMessage.PASSWORD_CHANGED));

        mockMvc.perform(post(Path.AUTH + "/password-reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(String.valueOf(HttpStatus.OK)))
                .andExpect(jsonPath("$.message").value(SuccessMessage.PASSWORD_CHANGED));
    }

    @Test
    void passwordReset_Failure() throws Exception {
        when(userService.changeUserPassword(any(ChangePasswordRequest.class)))
                .thenReturn(new MessageResponse(String.valueOf(HttpStatus.BAD_REQUEST), ErrorMessage.PASSWORD_INVALID));

        mockMvc.perform(post(Path.AUTH + "/password-reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.response").value(String.valueOf(HttpStatus.BAD_REQUEST)))
                .andExpect(jsonPath("$.message").value(ErrorMessage.PASSWORD_INVALID));
    }
}
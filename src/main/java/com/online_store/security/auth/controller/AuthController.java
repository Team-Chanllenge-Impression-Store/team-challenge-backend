package com.online_store.security.auth.controller;

import com.online_store.security.auth.dto.ChangePasswordRequest;
import com.online_store.security.auth.dto.LoginRequest;
import com.online_store.security.auth.dto.LoginResponse;
import com.online_store.security.auth.dto.SignupRequest;
import com.online_store.security.auth.service.AuthenticationService;
import com.online_store.utils.MessageResponse;
import com.online_store.utils.constant.Path;
import com.online_store.utils.constant.SuccessMessage;
import com.online_store.web.user.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * This controller provides APIs for register, login and logout actions.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(Path.AUTH)
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication and authorization related API endpoints")
public class AuthController {
    private final AuthenticationService authenticationService;


    /**
     * Sign-in API endpoint which is responsible for authenticating login request and setting JWT cookie
     *
     * @return response which contains JWT and UserDetails data
     */
    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public LoginResponse authenticateUser(@ModelAttribute LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    /**
     * Sign-up API endpoint which is responsible for creating a new user and saving it to DB
     *
     * @param signupRequest contains payload with the data from {@link SignupRequest}
     * @return status 200 if user is unique and has valid password
     */
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public MessageResponse registerUser(@ModelAttribute SignupRequest signupRequest) {
        final User registered = authenticationService.register(signupRequest);
        return new MessageResponse(String.valueOf(HttpStatus.OK), SuccessMessage.USER_CREATED);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        final ResponseCookie cookie = authenticationService.logout();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse(String.valueOf(HttpStatus.OK), SuccessMessage.USER_LOGGED_OUT));
    }

    /**
     * Password Reset API endpoint which is responsible for resetting a password for a user
     *
     * @param request contains payload with the data from {@link ChangePasswordRequest}
     * @return status 200 if passwords matches and user already exists
     */
    @PostMapping(value = "/password-reset", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> passwordReset(@ModelAttribute ChangePasswordRequest request) {
        final MessageResponse messageResponse = authenticationService.changeUserPassword(request);
        if (messageResponse.getResponseCode().equals(String.valueOf(HttpStatus.BAD_REQUEST))) {
            return ResponseEntity.badRequest().body(messageResponse);
        }
        return ResponseEntity.ok().body(messageResponse);
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestHeader(value = "Authorization") String authorizationHeader) {
        String refreshToken = authorizationHeader.substring("Bearer ".length());
        LoginResponse response = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}

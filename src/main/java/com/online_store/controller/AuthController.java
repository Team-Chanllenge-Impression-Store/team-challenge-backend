package com.online_store.controller;

import com.online_store.constants.ErrorMessage;
import com.online_store.constants.Path;
import com.online_store.constants.SuccessMessage;
import com.online_store.dto.request.ChangePasswordRequest;
import com.online_store.dto.request.LoginRequest;
import com.online_store.dto.request.SignupRequest;
import com.online_store.dto.response.MessageResponse;
import com.online_store.dto.response.UserInfoResponse;
import com.online_store.entity.User;
import com.online_store.security.jwt.JwtUtils;
import com.online_store.security.services.UserDetailsImpl;
import com.online_store.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller provides APIs for register, login and logout actions.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(Path.AUTH)
@Tag(name = "Auth", description = "Authentication and authorization related API endpoints")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Sign-in API endpoint which is responsible for authenticating login request and setting JWT cookie
     *
     * @return response which contains JWT and UserDetails data
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.findUserByEmail(loginRequest.getEmail());
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse(String.valueOf(HttpStatus.BAD_REQUEST), ErrorMessage.USER_NOT_FOUND));
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) userService.getUserPrincipal(loginRequest);
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getEmail(),
                        userDetails.isActive(),
                        roles));
    }

    /**
     * Sign-up API endpoint which is responsible for creating a new user and saving it to DB
     *
     * @param signupRequest contains payload with the data from {@link SignupRequest}
     * @return status 200 if user is unique and has valid password
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        MessageResponse response = userService.registerUser(signupRequest);
        if (response.getResponse().equals(String.valueOf(HttpStatus.BAD_REQUEST))) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse(String.valueOf(HttpStatus.OK), SuccessMessage.USER_LOGGED_OUT));
    }

    /**
     * Password Reset API endpoint which is responsible for resetting a password for a user
     *
     * @param request contains payload with the data from {@link ChangePasswordRequest}
     * @return status 200 if passwords matches and user already exists
     */
    @PostMapping("/password-reset")
    public ResponseEntity<?> passwordReset(@Valid @RequestBody ChangePasswordRequest request) {
        MessageResponse messageResponse = userService.changeUserPassword(request);
        if (messageResponse.getResponse().equals(String.valueOf(HttpStatus.BAD_REQUEST))) {
            return ResponseEntity.badRequest().body(messageResponse);
        }
        return ResponseEntity.ok().body(messageResponse);
    }
}

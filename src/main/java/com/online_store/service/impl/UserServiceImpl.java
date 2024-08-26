package com.online_store.service.impl;

import com.online_store.constants.ErrorMessage;
import com.online_store.constants.SuccessMessage;
import com.online_store.dto.request.ChangePasswordRequest;
import com.online_store.dto.request.LoginRequest;
import com.online_store.dto.request.SignupRequest;
import com.online_store.dto.response.MessageResponse;
import com.online_store.entity.Role;
import com.online_store.entity.User;
import com.online_store.repository.UserRepository;
import com.online_store.security.services.UserDetailsImpl;
import com.online_store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public MessageResponse registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new MessageResponse(String.valueOf(HttpStatus.BAD_REQUEST), ErrorMessage.EMAIL_IN_USE);
        }
        if (signupRequest.getPassword() != null && !signupRequest.getPassword().equals(signupRequest.getPassword2())) {
            new MessageResponse(String.valueOf(HttpStatus.BAD_REQUEST), ErrorMessage.PASSWORDS_DO_NOT_MATCH);
        }
        // create user
        User user = modelMapper.map(signupRequest, User.class);
        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        logger.info("Registered user: {}", user.getEmail());
        // save user to DB
        userRepository.save(user);

        return new MessageResponse(String.valueOf(HttpStatus.OK), SuccessMessage.USER_CREATED);
    }

    @Override
    @Transactional
    public MessageResponse changeUserPassword(ChangePasswordRequest request) {
        if (!request.getPassword().equals(request.getPassword2())) {
            return new MessageResponse(String.valueOf(HttpStatus.BAD_REQUEST), ErrorMessage.PASSWORDS_DO_NOT_MATCH);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND));

            user.setPassword(encoder.encode(request.getPassword()));
            logger.info("Password changed for user: {}", user.getEmail());
            userRepository.save(user);
            return new MessageResponse(String.valueOf(HttpStatus.OK), SuccessMessage.PASSWORD_CHANGED);
        }
        return new MessageResponse(String.valueOf(HttpStatus.BAD_REQUEST), ErrorMessage.USER_NOT_FOUND);
    }

    /**
     * Looks for user with specified email address
     *
     * @param email user's email address
     * @return user object {@link User}
     * @throws UsernameNotFoundException
     */
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND));
    }

    @Override
    public UserDetails getUserPrincipal(LoginRequest loginRequest) {
        // authenticate { username, password }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        // update SecurityContext using Authentication object
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // get UserDetails from Authentication object
        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails;
    }
}

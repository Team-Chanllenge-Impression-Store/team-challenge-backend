package com.online_store.web.user.service;

import com.online_store.security.auth.dto.ChangePasswordRequest;
import com.online_store.security.auth.dto.SignupRequest;
import com.online_store.utils.MessageResponse;
import com.online_store.utils.constant.ErrorMessage;
import com.online_store.utils.constant.SuccessMessage;
import com.online_store.utils.exception.EmailAlreadyExistsException;
import com.online_store.web.user.dao.UserRepository;
import com.online_store.web.user.model.Role;
import com.online_store.web.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(user.getEmail()))) {
            throw new EmailAlreadyExistsException(ErrorMessage.EMAIL_IN_USE);
        }
        return userRepository.save(user);
    }

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
        log.info("Registered user: {}", user.getEmail());
        // save user to DB
        userRepository.save(user);

        return new MessageResponse(String.valueOf(HttpStatus.OK), SuccessMessage.USER_CREATED);
    }

    @Override
    @Transactional
    public MessageResponse changeUserPassword(ChangePasswordRequest request) {
        // null checks and validations for old and new passwords
        if (request.getPassword() == null || request.getPassword2() == null
                || request.getPassword().isEmpty() || request.getPassword2().isEmpty()) {
            return new MessageResponse(String.valueOf(HttpStatus.BAD_REQUEST), ErrorMessage.PASSWORDS_CANT_BE_EMPTY);
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND));

        // check if old password matches
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            return new MessageResponse(String.valueOf(HttpStatus.BAD_REQUEST), ErrorMessage.INCORRECT_OLD_PASSWORD);
        }

        // change password
        user.setPassword(encoder.encode(request.getPassword2()));
        log.info("Password changed for user: {}", user.getEmail());
        userRepository.save(user);
        return new MessageResponse(String.valueOf(HttpStatus.OK), SuccessMessage.PASSWORD_CHANGED);
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByEmail(username);
    }
}

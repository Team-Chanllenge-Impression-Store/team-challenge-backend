package com.online_store.utils;

import com.online_store.utils.exception.ConflictException;
import com.online_store.utils.exception.EmailAlreadyExistsException;
import com.online_store.utils.exception.JwtTokenException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<MessageResponse> handleJwtTokenException(JwtTokenException ex) {
        return new ResponseEntity<>(new MessageResponse(
                String.valueOf(HttpStatus.FORBIDDEN), ex.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MessageResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(new MessageResponse(
                String.valueOf(HttpStatus.NOT_FOUND), ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<MessageResponse> handleMissingRequestAuthorizationHeaderException(MissingRequestHeaderException ex) {
        return new ResponseEntity<>(new MessageResponse(
                String.valueOf(HttpStatus.BAD_REQUEST), "Missing Authorization Header"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<MessageResponse> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>(new MessageResponse(
                String.valueOf(HttpStatus.UNAUTHORIZED), ex.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<MessageResponse> handleConflictException(ConflictException ex) {
        return new ResponseEntity<>(new MessageResponse(
                String.valueOf(HttpStatus.CONFLICT), ex.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<MessageResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return new ResponseEntity<>(new MessageResponse(
                String.valueOf(HttpStatus.BAD_REQUEST), ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<MessageResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ResponseEntity<>(new MessageResponse(
                String.valueOf(HttpStatus.BAD_REQUEST), ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}

package com.online_store.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class JwtTokenException extends RuntimeException {
    public JwtTokenException(String message) {
        super(message);
    }
}

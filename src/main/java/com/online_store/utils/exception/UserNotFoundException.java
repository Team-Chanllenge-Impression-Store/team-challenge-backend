package com.online_store.utils.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userNotFound) {
        super(userNotFound);
    }
}

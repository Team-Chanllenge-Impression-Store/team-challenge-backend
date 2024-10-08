package com.online_store.utils.constant;

public class ErrorMessage {
    public static final String EMAIL_NOT_FOUND = "Email not found";
    public static final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match.";
    public static final String EMAIL_IN_USE = "Email is already in use.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String EMPTY_FIRST_NAME = "First name cannot be empty";
    public static final String EMPTY_LAST_NAME = "Last name cannot be empty";
    public static final String PASSWORD_INVALID = "Invalid old password";
    public static final String PASSWORD_CHARACTER_LENGTH = "The password must be between 6 and 16 characters long";
    public static final String PASSWORD2_CHARACTER_LENGTH = "The password confirmation must be between 6 and 16 characters long";
    public static final String INCORRECT_EMAIL = "Incorrect email";
    public static final String EMAIL_CANNOT_BE_EMPTY = "Email cannot be empty";
    public static final String FILL_IN_THE_INPUT_FIELD = "Fill in the input field";
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String INVALID_CATEGORY_NAME = "Category with such name doesn't exist";
    public static final String PRODUCT_NOT_FOUND = "Category not found";
    public static final String USER_NOT_ACTIVE = "User's email address has not been confirmed.";
    public static final String PASSWORDS_CANT_BE_EMPTY = "Passwords can't be empty";
    public static final String INCORRECT_OLD_PASSWORD = "Incorrect old password";
    // for JWT
    public static final String INVALID_REFRESH_TOKEN = "Only refresh token is allowed";
    public static final String MISS_BEARER_PREFIX = "Missing 'Bearer' type in 'Authorization' header. Expected 'Authorization: Bearer <JWT>'";
    public static final String TOKEN_EXPIRED = "JWT token is expired";
    public static final String TOKEN_INVALID = "JWT token is invalid";

    private ErrorMessage() {
    }
}

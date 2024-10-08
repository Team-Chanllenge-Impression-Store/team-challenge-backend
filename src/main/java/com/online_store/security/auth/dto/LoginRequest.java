package com.online_store.security.auth.dto;

import com.online_store.utils.constant.ErrorMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

    @Email(message = ErrorMessage.INCORRECT_EMAIL)
    @NotBlank(message = ErrorMessage.EMAIL_CANNOT_BE_EMPTY)
    @NotNull
    private String email;

    @Size(min = 6, max = 16, message = ErrorMessage.PASSWORD_CHARACTER_LENGTH)
    @NotBlank
    @NotNull
    private String password;
}

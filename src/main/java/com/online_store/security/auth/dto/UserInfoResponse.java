package com.online_store.security.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private String email;
    private Boolean active;
    private List<String> roles;
}

package com.synergy.synlabs.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}

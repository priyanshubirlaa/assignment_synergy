package com.synergy.synlabs.dto;

import com.synergy.synlabs.model.UserType;
import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private UserType userType;
    private String profileHeadline;
    private String address;
}

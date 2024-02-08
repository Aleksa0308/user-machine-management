package com.raf.usermanagement.requests;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private Set<String> permissions;
}

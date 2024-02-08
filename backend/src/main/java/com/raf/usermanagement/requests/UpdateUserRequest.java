package com.raf.usermanagement.requests;

import com.raf.usermanagement.model.Permission;
import lombok.Data;

@Data
public class UpdateUserRequest{
    private Long userId;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String[] permissions;
}

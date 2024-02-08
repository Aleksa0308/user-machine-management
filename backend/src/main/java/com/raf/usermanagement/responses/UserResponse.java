package com.raf.usermanagement.responses;

import com.raf.usermanagement.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Data
@Getter
@Setter
public class UserResponse {
    private Optional<User> user;
}

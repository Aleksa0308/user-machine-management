package com.raf.usermanagement.responses;

import com.raf.usermanagement.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class UsersResponse {
    private List<User> users;
}

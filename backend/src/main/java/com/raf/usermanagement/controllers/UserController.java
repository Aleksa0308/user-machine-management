package com.raf.usermanagement.controllers;

import com.raf.usermanagement.model.User;
import com.raf.usermanagement.requests.RegisterRequest;
import com.raf.usermanagement.requests.UpdateUserRequest;
import com.raf.usermanagement.responses.UserResponse;
import com.raf.usermanagement.responses.UsersResponse;
import com.raf.usermanagement.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
     try {
         userService.register(
                 registerRequest.getFirstname(),
                 registerRequest.getLastname(),
                 registerRequest.getEmail(),
                 registerRequest.getPassword(),
                 registerRequest.getPermissions()
         );
     } catch (Exception e) {
         e.printStackTrace();
         return ResponseEntity.status(401).build();
     }
     return ResponseEntity.ok().build();
 }

    @GetMapping("/get")
    public ResponseEntity<UsersResponse> getUsers(){
        List<User> users = userService.getUsers();
        UsersResponse usersResponse = new UsersResponse();
        usersResponse.setUsers(users);
        return ResponseEntity.ok().body(usersResponse);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Optional<UserResponse>> getUser(@PathVariable("id") Long id){
        Optional<User> user = userService.getUserById(id);
        UserResponse userResponse = new UserResponse();
        userResponse.setUser(user);
        return ResponseEntity.ok().body(Optional.of(userResponse));
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody UpdateUserRequest updateUserRequest){
        try {
            User updatedUser =  userService.updateUser(updateUserRequest);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id){
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}


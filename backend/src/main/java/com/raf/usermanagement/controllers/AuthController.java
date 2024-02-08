package com.raf.usermanagement.controllers;

import com.raf.usermanagement.model.Permission;
import com.raf.usermanagement.requests.LoginRequest;
import com.raf.usermanagement.responses.LoginResponse;
import com.raf.usermanagement.services.UserService;
import com.raf.usermanagement.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        // Fetch user's permissions from the UserService
        List<String> permissions = userService.getUserPermissionsByEmail(loginRequest.getEmail());

        // Generate JWT token with username and permissions
        String token = jwtUtil.generateToken(loginRequest.getEmail(), permissions);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}

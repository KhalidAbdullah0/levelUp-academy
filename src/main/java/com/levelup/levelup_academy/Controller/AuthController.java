package com.levelup.levelup_academy.Controller;

import com.levelup.levelup_academy.DTO.LoginRequest;
import com.levelup.levelup_academy.DTOOut.AuthResponse;
import com.levelup.levelup_academy.DTOOut.UserMeResponse;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        
        // Check if Pro Player or Trainer is approved
        if (user.getRole().equals("PRO")) {
            if (user.getPro() == null || !user.getPro().getIsApproved()) {
                throw new org.springframework.security.authentication.BadCredentialsException(
                    "Your account is pending admin approval. Please wait for approval before logging in."
                );
            }
        } else if (user.getRole().equals("TRAINER")) {
            if (user.getTrainer() == null || !user.getTrainer().getIsApproved()) {
                throw new org.springframework.security.authentication.BadCredentialsException(
                    "Your account is pending admin approval. Please wait for approval before logging in."
                );
            }
        }
        
        String token = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setExpiresIn(jwtService.getExpirationInMs());
        response.setRole(user.getRole());
        
        // Add user info
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRole(user.getRole());
        userInfo.setFullName(user.getFirstName() + " " + user.getLastName());
        userInfo.setEmail(user.getEmail());
        response.setUser(userInfo);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        UserMeResponse response = new UserMeResponse();
        response.setId(user.getId());
        response.setFullName(user.getFirstName() + " " + user.getLastName());
        response.setRole(user.getRole());
        response.setEmail(user.getEmail());
        
        return ResponseEntity.ok(response);
    }
}

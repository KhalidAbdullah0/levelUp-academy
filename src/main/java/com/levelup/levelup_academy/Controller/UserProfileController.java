package com.levelup.levelup_academy.Controller;

import com.levelup.levelup_academy.Api.ApiException;
import com.levelup.levelup_academy.Api.ApiResponse;
import com.levelup.levelup_academy.DTO.*;
import com.levelup.levelup_academy.DTOOut.PaymentResponseDTO;
import com.levelup.levelup_academy.DTOOut.UserMeResponse;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Repository.AuthRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class UserProfileController {

    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
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

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ProfileUpdateDTO profileDTO) {
        if (user == null) {
            return ResponseEntity.status(401).body(new ApiResponse("Unauthorized"));
        }

        // Reload user from database to ensure we have latest data
        User currentUser = authRepository.findUserById(user.getId());
        if (currentUser == null) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found"));
        }

        currentUser.setFirstName(profileDTO.getFirstName());
        currentUser.setLastName(profileDTO.getLastName());
        authRepository.save(currentUser);

        return ResponseEntity.ok(new ApiResponse("Profile updated successfully"));
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponse> updatePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PasswordUpdateDTO passwordDTO) {
        if (user == null) {
            return ResponseEntity.status(401).body(new ApiResponse("Unauthorized"));
        }

        // Reload user from database
        User currentUser = authRepository.findUserById(user.getId());
        if (currentUser == null) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found"));
        }

        // Verify current password
        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), currentUser.getPassword())) {
            return ResponseEntity.status(400).body(new ApiResponse("Current password is incorrect"));
        }

        // Update password
        String hashedPassword = passwordEncoder.encode(passwordDTO.getNewPassword());
        currentUser.setPassword(hashedPassword);
        authRepository.save(currentUser);

        return ResponseEntity.ok(new ApiResponse("Password updated successfully"));
    }

    @PutMapping("/contact")
    public ResponseEntity<ApiResponse> updateContact(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ContactUpdateDTO contactDTO) {
        if (user == null) {
            return ResponseEntity.status(401).body(new ApiResponse("Unauthorized"));
        }

        // Reload user from database
        User currentUser = authRepository.findUserById(user.getId());
        if (currentUser == null) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found"));
        }

        // Check if email is already in use by another user
        User existingUser = authRepository.findUserByEmail(contactDTO.getEmail());
        if (existingUser != null && !existingUser.getId().equals(currentUser.getId())) {
            return ResponseEntity.status(400).body(new ApiResponse("Email is already in use"));
        }

        currentUser.setEmail(contactDTO.getEmail());
        // Note: phoneNumber would need to be added to User model if needed
        authRepository.save(currentUser);

        return ResponseEntity.ok(new ApiResponse("Contact information updated successfully"));
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentHistory(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        // For now, return empty list - implement payment history storage if needed
        // This would typically query a Payment entity/table
        List<PaymentResponseDTO> payments = new ArrayList<>();
        
        // TODO: Implement actual payment history retrieval from database
        // Example:
        // List<Payment> userPayments = paymentRepository.findByUserId(user.getId());
        // payments = userPayments.stream().map(p -> new PaymentResponseDTO(...)).collect(...);
        
        return ResponseEntity.ok(payments);
    }

    @PostMapping("/payments")
    public ResponseEntity<PaymentResponseDTO> processPayment(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PaymentRequestDTO paymentDTO) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        // Process payment using token (not storing card details)
        // This is a simplified version - integrate with your actual payment service
        try {
            // Use payment token to process payment
            // For now, create a mock response
            PaymentResponseDTO response = new PaymentResponseDTO();
            response.setId(1); // Would be from database
            response.setPaymentReference("PAY-" + System.currentTimeMillis());
            response.setAmount(paymentDTO.getAmount());
            response.setStatus("SUCCESS");
            response.setDescription(paymentDTO.getDescription());
            response.setCreatedAt(LocalDateTime.now());
            response.setUpdatedAt(LocalDateTime.now());

            // TODO: Actually process payment with payment provider (Stripe, Moyasar, etc.)
            // TODO: Store payment reference in database (not card details)
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ApiException("Payment processing failed: " + e.getMessage());
        }
    }
}


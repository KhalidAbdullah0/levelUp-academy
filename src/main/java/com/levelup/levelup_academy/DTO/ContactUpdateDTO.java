package com.levelup.levelup_academy.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactUpdateDTO {
    @NotEmpty(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    private String phoneNumber; // Optional
}





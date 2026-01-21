package com.levelup.levelup_academy.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateDTO {
    @NotEmpty(message = "Current password is required")
    private String currentPassword;
    
    @NotEmpty(message = "New password is required")
    @Size(min = 8, max = 200, message = "Password must be at least 8 characters")
    private String newPassword;
}





package com.levelup.levelup_academy.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDTO {
    @NotEmpty(message = "First name cannot be empty")
    @Size(min = 2, max = 40, message = "First name must be between 2 and 40 characters")
    private String firstName;
    
    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 2, max = 40, message = "Last name must be between 2 and 40 characters")
    private String lastName;
}





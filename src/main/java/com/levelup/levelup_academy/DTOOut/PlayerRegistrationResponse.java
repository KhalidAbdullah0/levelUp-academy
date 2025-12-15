package com.levelup.levelup_academy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRegistrationResponse {
    private Integer id;
    private String username;
    private String email;
    private String role;
}


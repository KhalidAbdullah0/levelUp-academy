package com.levelup.levelup_academy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionWithUserDTO {
    private Integer subscriptionId;
    private Integer userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String packageType;
    private Integer sessionCount;
    private Integer price;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
}


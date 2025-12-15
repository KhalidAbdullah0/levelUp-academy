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
public class PlayerWithSubscriptionDTO {
    private Integer playerId;
    private Integer userId;
    private String username;
    private String fullName;
    private String email;
    private LocalDate registeredAt;
    private String currentPlan; // BASIC, STANDARD, PREMIUM, NONE
    private String subscriptionStatus; // NONE, PENDING_PAYMENT, ACTIVE, EXPIRED, CANCELED
    private String paymentStatus; // NONE, UNPAID, PARTIALLY_PAID, PAID
    private Integer totalClasses;
    private Integer usedClasses;
}


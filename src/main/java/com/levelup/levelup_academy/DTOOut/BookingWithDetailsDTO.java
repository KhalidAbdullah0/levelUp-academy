package com.levelup.levelup_academy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingWithDetailsDTO {
    private Integer bookingId;
    private Integer userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Integer subscriptionId;
    private String subscriptionPackage;
    private Integer subscriptionPrice;
    private String subscriptionStatus;
    private Integer sessionId;
    private String sessionName;
    private String gameName;
    private LocalDateTime sessionStartDate;
    private LocalDateTime sessionEndDate;
    private String sessionTime;
    private LocalDateTime bookDate;
    private String bookingStatus;
}


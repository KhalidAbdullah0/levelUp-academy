package com.levelup.levelup_academy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDetailsDTO {
    private PlayerInfo player;
    private List<SubscriptionInfo> subscriptions;
    private List<PaymentInfo> payments;
    private List<BookingInfo> bookings;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayerInfo {
        private Integer playerId;
        private Integer userId;
        private String username;
        private String fullName;
        private String email;
        private LocalDate registeredAt;
        private String status; // ACTIVE, BLOCKED
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscriptionInfo {
        private Integer subscriptionId;
        private String plan; // BASIC, STANDARD, PREMIUM
        private Integer classesCount;
        private Integer price;
        private String currency;
        private String status; // PENDING_PAYMENT, ACTIVE, EXPIRED, CANCELED
        private LocalDate startDate;
        private LocalDate endDate;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentInfo {
        private Integer paymentId;
        private Integer subscriptionId;
        private Double amount;
        private String currency;
        private String status; // SUCCESS, FAILED, REFUNDED
        private LocalDateTime paidAt;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingInfo {
        private Integer bookingId;
        private Integer sessionId;
        private LocalDateTime sessionStartTime;
        private LocalDateTime sessionEndTime;
        private String trainerName;
        private String plan;
        private String status; // UPCOMING, COMPLETED, CANCELED
    }
}


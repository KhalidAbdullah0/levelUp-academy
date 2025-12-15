package com.levelup.levelup_academy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentChargeRequest {
    private Integer subscriptionId;
    private Integer sessionId;
    private Double amount;
    private String currency;
    private CardDetails card;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardDetails {
        private String cardNumber;
        private String cardHolderName;
        private Integer expiryMonth;
        private Integer expiryYear;
        private String cvv;
    }
}


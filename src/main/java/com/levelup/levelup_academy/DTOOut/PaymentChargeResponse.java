package com.levelup.levelup_academy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentChargeResponse {
    private Integer paymentId;
    private String status;
    private Integer subscriptionId;
    private Integer sessionId;
    private Integer bookingId;
}


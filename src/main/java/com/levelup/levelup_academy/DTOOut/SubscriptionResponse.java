package com.levelup.levelup_academy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {
    private Integer subscriptionId;
    private Integer playerId;
    private String plan;
    private Integer classesCount;
    private Integer price;
    private String currency;
    private String status;
}


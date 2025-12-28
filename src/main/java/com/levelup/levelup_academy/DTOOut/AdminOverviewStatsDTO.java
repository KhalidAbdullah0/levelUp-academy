package com.levelup.levelup_academy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminOverviewStatsDTO {
    private Integer totalPlayers;
    private Integer totalTrainers;
    private Integer totalProPlayers;
    private Integer totalParents;
    private Integer activeSubscriptions;
    private Double totalRevenue;
    private String currency;
}


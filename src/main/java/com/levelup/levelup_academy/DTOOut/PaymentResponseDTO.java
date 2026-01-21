package com.levelup.levelup_academy.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Integer id;
    private String paymentReference;
    private Double amount;
    private String status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}





package com.example.demo.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFailedEvent {
    private Long paymentId;
    private String status;
    private BigDecimal amount;
    private String payment_method;
    private String createdAt;
    private String updatedAt;
    private String failureReason;
}

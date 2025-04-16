package com.example.demo.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCompletedEvent {
    private Long bookingId;
    private BigDecimal number;
    private String status;
    private String createdAt;
    private String updatedAt;

}

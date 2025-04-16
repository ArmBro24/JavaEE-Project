package com.example.demo.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Number is required")
    @Positive(message = "Number must be positive")
    private BigDecimal number;
}

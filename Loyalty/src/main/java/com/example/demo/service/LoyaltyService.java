package com.example.demo.service;

import java.math.BigDecimal;

public interface LoyaltyService {
    void accruePoints(Long bookingId, BigDecimal amount);
    Integer getTotalPoints(Long bookingId);
}

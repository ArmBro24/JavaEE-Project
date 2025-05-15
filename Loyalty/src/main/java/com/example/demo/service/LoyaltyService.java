package com.example.demo.service;

import java.math.BigDecimal;

public interface LoyaltyService {
    void registerUser(Long userId);
    void accruePoints(Long userId, BigDecimal amount, Long bookingId);
    Integer getBalance(Long userId);
}

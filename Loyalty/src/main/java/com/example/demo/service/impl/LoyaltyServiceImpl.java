package com.example.demo.service.impl;

import com.example.demo.entity.LoyaltyTransaction;
import com.example.demo.repository.LoyaltyTransactionRepository;
import com.example.demo.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoyaltyServiceImpl implements LoyaltyService {

    private final LoyaltyTransactionRepository transactionRepo;

    @Override
    public void accruePoints(Long bookingId, BigDecimal amount) {
        int points = amount.divide(BigDecimal.valueOf(100)).intValue();
        LoyaltyTransaction transaction = LoyaltyTransaction.builder()
                .bookingId(bookingId)
                .changeAmount(points)
                .type("ACCRUE")
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepo.save(transaction);
        log.info("✅ Loyalty points accrued: {} points for booking {}", points, bookingId);
    }

    @Override
    public Integer getTotalPoints(Long bookingId) {
        return transactionRepo.findAll().stream()
                .filter(t -> bookingId.equals(t.getBookingId()))
                .mapToInt(t -> "ACCRUE".equals(t.getType()) ? t.getChangeAmount() : -t.getChangeAmount())
                .sum();
    }
}

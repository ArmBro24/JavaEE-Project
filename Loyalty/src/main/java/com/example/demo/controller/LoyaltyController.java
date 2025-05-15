package com.example.demo.controller;



import com.example.demo.repository.LoyaltyTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loyalty")
@RequiredArgsConstructor
public class LoyaltyController {

    private final LoyaltyTransactionRepository transactionRepository;

    @GetMapping("/points/{bookingId}")
    public Integer getPointsByBooking(@PathVariable Long bookingId) {
        return transactionRepository.findAll().stream()
                .filter(tx -> tx.getBookingId().equals(bookingId))
                .mapToInt(tx -> "ACCRUE".equals(tx.getType()) ? tx.getChangeAmount() : -tx.getChangeAmount())
                .sum();
    }
}


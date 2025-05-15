package com.example.demo.service.impl;

import com.example.demo.repository.LoyaltyAccountRepository;
import com.example.demo.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoyaltyServiceImpl implements LoyaltyService {
    private final LoyaltyAccountRepository accountRepo;
    private final LoyaltyTransactionRepository transactionRepo;

    @Override
    public void registerUser(Long userId) {
        if (accountRepo.findByUserId(userId).isPresent()) return;
        accountRepo.save(LoyaltyAccount.builder()
                .userId(userId)
                .balance(0)
                .build());
    }

    @Override
    public void accruePoints(Long userId, BigDecimal amount, Long bookingId) {
        LoyaltyAccount account = accountRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int points = amount.divide(BigDecimal.valueOf(100)).intValue(); // 1 point per 100₸

        account.setBalance(account.getBalance() + points);
        accountRepo.save(account);

        transactionRepo.save(LoyaltyTransaction.builder()
                .loyaltyId(account.getLoyaltyId())
                .bookingId(bookingId)
                .changeAmount(points)
                .type("ACCRUE")
                .build());
    }

    @Override
    public Integer getBalance(Long userId) {
        return accountRepo.findByUserId(userId)
                .map(LoyaltyAccount::getBalance)
                .orElse(0);
    }
}

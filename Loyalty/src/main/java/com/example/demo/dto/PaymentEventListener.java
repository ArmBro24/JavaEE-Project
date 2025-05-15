package com.example.demo.dto;

import com.example.demo.dto.PaymentCreatedEvent;
import com.example.demo.entity.LoyaltyTransaction;
import com.example.demo.repository.LoyaltyTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final LoyaltyTransactionRepository loyaltyTransactionRepository;

    @KafkaListener(topics = "payment-topic")
    public void listen(PaymentCreatedEvent event) {
        if ("COMPLETED".equalsIgnoreCase(event.getStatus())) {
            log.info("✅ Payment received for loyalty → paymentId: {}, bookingId: {}, amount: {}",
                    event.getPaymentId(), event.getBookingId(), event.getAmount());

            int points = event.getAmount().divideToIntegralValue(new java.math.BigDecimal("10")).intValue();

            LoyaltyTransaction tx = LoyaltyTransaction.builder()
                    .bookingId(event.getBookingId())
                    .changeAmount(points)
                    .type("ACCRUE")
                    .createdAt(LocalDateTime.now())
                    .build();

            loyaltyTransactionRepository.save(tx);
            log.info("🎯 Loyalty transaction saved: {} points for booking {}", points, event.getBookingId());
        } else {
            log.warn("⚠️ Ignored non-COMPLETED payment: {}", event);
        }
    }
}

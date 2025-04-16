package com.example.demo.service.impl;

import com.example.demo.entity.BookingStorage;
import com.example.demo.entity.Payment;
import com.example.demo.repository.BookingStorageRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.request.PaymentRequest;
import com.example.demo.response.BookingEventListener;
import com.example.demo.response.PaymentCompletedEvent;
import com.example.demo.response.PaymentFailedEvent;
import com.example.demo.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingStorageRepository bookingStorageRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String payment_topic = "payment-topic";

    @Override
    @Transactional
    public Object initiatePayment(PaymentRequest request) {
        Long bookingId = BookingEventListener.latestBookingId;

        if (bookingId == null) {
            log.error("❌ No booking ID available in memory");
            return "Error: No valid booking ID found!";
        }
        log.info("🧠 Using in-memory bookingId: {}", bookingId);


        Payment newPayment = Payment.builder()
                .bookingId(bookingId)
                .amount(request.getAmount())
                .payment_method(request.getPaymentMethod())
                .status("PROCESSING")
                .createdAt(LocalDateTime.now())
                .build();

        newPayment = paymentRepository.save(newPayment);

        boolean paymentSuccess = simulatePaymentProcessing();
        newPayment.setStatus(paymentSuccess ? "COMPLETED" : "FAILED");
        newPayment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(newPayment);

        return publishPaymentEvent(newPayment);
    }

    @Override
    public Object getPaymentDetails(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if ("COMPLETED".equals(payment.getStatus())) {
            return PaymentCompletedEvent.builder()
                    .paymentId(payment.getId())
                    .status(payment.getStatus())
                    .amount(payment.getAmount())
                    .payment_method(payment.getPayment_method())
                    .createdAt(payment.getCreatedAt().toString())
                    .updatedAt(payment.getUpdatedAt().toString())
                    .build();
        } else {
            return PaymentFailedEvent.builder()
                    .paymentId(payment.getId())
                    .status(payment.getStatus())
                    .amount(payment.getAmount())
                    .payment_method(payment.getPayment_method())
                    .createdAt(payment.getCreatedAt().toString())
                    .updatedAt(payment.getUpdatedAt().toString())
                    .failureReason("Payment processing failed")
                    .build();
        }
    }

    private Object publishPaymentEvent(Payment payment) {
        if ("COMPLETED".equals(payment.getStatus())) {
            PaymentCompletedEvent event = PaymentCompletedEvent.builder()
                    .paymentId(payment.getId())
                    .status(payment.getStatus())
                    .amount(payment.getAmount())
                    .payment_method(payment.getPayment_method())
                    .createdAt(LocalDateTime.now().toString())
                    .updatedAt(LocalDateTime.now().toString())
                    .build();

            try {
                kafkaTemplate.send(payment_topic, event).get();
                log.info("✅ Published PaymentCompletedEvent: {}", event);
            } catch (Exception e) {
                log.error("❌ Failed to publish PaymentCompletedEvent", e);
            }
            return event;

        } else {
            PaymentFailedEvent event = PaymentFailedEvent.builder()
                    .paymentId(payment.getId())
                    .status(payment.getStatus())
                    .amount(payment.getAmount())
                    .payment_method(payment.getPayment_method())
                    .createdAt(LocalDateTime.now().toString())
                    .updatedAt(LocalDateTime.now().toString())
                    .failureReason("Payment processing failed")
                    .build();

            try {
                kafkaTemplate.send(payment_topic, event).get();
                log.info("✅ Published PaymentFailedEvent: {}", event);
            } catch (Exception e) {
                log.error("❌ Failed to publish PaymentFailedEvent", e);
            }
            return event;
        }
    }

    private boolean simulatePaymentProcessing() {
        return Math.random() > 0.2;
    }
}

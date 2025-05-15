package com.example.demo.service.impl;

import com.example.demo.entity.Payment;
import com.example.demo.entity.Refund;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.RefundRepository;
import com.example.demo.request.RefundRequest;
import com.example.demo.response.RefundResponse;
import com.example.demo.service.RefundService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public RefundResponse processRefund(RefundRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if(!"COMPLETED".equals(payment.getStatus())){
            throw new IllegalStateException("Cannot refund a payment that is not completed");
        }

        Refund refund = Refund.builder()
                .amount(request.getAmount())
                .reason(request.getReason())
                .status("INITIATED")
                .build();

        refund = refundRepository.save(refund);

        boolean refundSuccess = simulateRefundProcessing();

        refund.setStatus(refundSuccess ? "COMPLETED" : "FAILED");
        refundRepository.save(refund);

        publishRefundEvent(refund);

        return RefundResponse.builder()
                .refundId(refund.getId())
                .status(refund.getStatus())
                .build();
    }

    private boolean simulateRefundProcessing() {
        return Math.random() > 0.1;
    }

    private void publishRefundEvent(Refund refund) {
        System.out.println("Publishing refund event for refund ID: " + refund.getId());
    }
}

package com.example.demo.service;

import com.example.demo.request.PaymentRequest;

public interface PaymentService {
    Object initiatePayment(PaymentRequest request);
    Object getPaymentDetails(Long paymentId);
}

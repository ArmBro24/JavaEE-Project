package com.example.demo.controllers;

import com.example.demo.request.PaymentRequest;
import com.example.demo.response.PaymentCompletedEvent;
import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Object> initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        Object paymentEvent = paymentService.initiatePayment(paymentRequest);

        if (paymentEvent instanceof PaymentCompletedEvent) {
            return ResponseEntity.ok(paymentEvent);
        } else {
            return ResponseEntity.badRequest().body(paymentEvent);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getPaymentDetails(@PathVariable Long paymentId){
        Object paymentEvent = paymentService.getPaymentDetails(paymentId);

        if (paymentEvent instanceof PaymentCompletedEvent) {
            return ResponseEntity.ok(paymentEvent);
        } else {
            return ResponseEntity.badRequest().body(paymentEvent);
        }
    }
}

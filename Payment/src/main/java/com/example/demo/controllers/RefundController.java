package com.example.demo.controllers;

import com.example.demo.request.RefundRequest;
import com.example.demo.response.RefundResponse;
import com.example.demo.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments/refunds")
@RequiredArgsConstructor
public class RefundController {

    private RefundService refundService;

    @PostMapping
    public ResponseEntity<RefundResponse> processRefund(@RequestBody RefundRequest refundRequest){
        RefundResponse refundResponse = refundService.processRefund(refundRequest);
        return ResponseEntity.ok(refundResponse);
    }
}

package com.example.demo.service;

import com.example.demo.request.RefundRequest;
import com.example.demo.response.RefundResponse;

public interface RefundService {
    RefundResponse processRefund(RefundRequest request);
}

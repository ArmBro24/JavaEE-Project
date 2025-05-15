package com.example.demo.controller;

import com.example.demo.request.BookingRequest;
import com.example.demo.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Object> createBooking(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingDetails(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBookingDetails(bookingId));
    }
}

package com.example.demo.service;

import com.example.demo.request.BookingRequest;

public interface BookingService {
    Object createBooking(BookingRequest request);
    Object getBookingDetails(Long bookingId);
}

package com.example.demo.service.impl;

import com.example.demo.entity.Booking;
import com.example.demo.repository.BookingRepository;
import com.example.demo.request.BookingRequest;
import com.example.demo.response.BookingCompletedEvent;
import com.example.demo.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String booking_topic = "booking-topic";

    @Override
    @Transactional
    public Object createBooking(BookingRequest request) {
        Booking booking = Booking.builder()
                .name(request.getName())
                .number(request.getNumber())
                .status("COMPLETED")
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        return publishBookingEvent(savedBooking);
    }


    @Override
    public Object getBookingDetails(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    private Object publishBookingEvent(Booking booking) {
        BookingCompletedEvent event = BookingCompletedEvent.builder()
                .bookingId(booking.getId())
                .number(booking.getNumber())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt().toString())
                .updatedAt(booking.getUpdatedAt().toString())
                .build();

        try {
            kafkaTemplate.send(booking_topic, event).get();
            log.info("✅ Published BookingCompletedEvent: {}", event);
        } catch (Exception e) {
            log.error("❌ Failed to publish BookingCompletedEvent", e);
        }

        return event;
    }
}

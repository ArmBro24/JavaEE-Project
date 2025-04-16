package com.example.demo.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookingEventListener {

    public static volatile Long latestBookingId;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "booking-topic")
    public void listen(BookingCreatedEvent event) {
        if (event.getBookingId() != null) {
            latestBookingId = event.getBookingId();
            log.info("✅ Latest bookingId set to: {}", latestBookingId);
        } else {
            log.warn("⚠️ Booking event has no ID");
        }
    }

}

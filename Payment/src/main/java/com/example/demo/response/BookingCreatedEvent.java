package com.example.demo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCreatedEvent {
    @JsonProperty("bookingId")
    private Long bookingId;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("flightId")
    private Long flightId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("createdAt")
    private String createdAt;
}

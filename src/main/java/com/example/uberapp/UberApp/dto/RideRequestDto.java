package com.example.uberapp.UberApp.dto;

import java.time.LocalDateTime;

import com.example.uberapp.UberApp.entities.Rider;
import com.example.uberapp.UberApp.entities.enums.PaymentMethod;
import com.example.uberapp.UberApp.entities.enums.RideRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {

    private Long Id;
    private PointDto pickupLocation;
    private PointDto dropoffLocation;
    private LocalDateTime requestedTime;
    private Rider rider;
    private PaymentMethod paymentMethod;
    private RideRequestStatus rideRequestStatus;
    private Double fare;
}

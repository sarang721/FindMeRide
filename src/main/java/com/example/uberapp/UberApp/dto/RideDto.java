package com.example.uberapp.UberApp.dto;

import java.time.LocalDateTime;

import com.example.uberapp.UberApp.entities.enums.PaymentMethod;
import com.example.uberapp.UberApp.entities.enums.RideStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDto {

    private Long Id;
    private RiderDto rider;
    private DriverDto driver;
    private PointDto pickupLocation;
    private PointDto dropoffLocation;
    private PaymentMethod paymentMethod;
    private RideStatus rideStatus;
    private Double fare;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}

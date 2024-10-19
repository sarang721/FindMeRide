package com.example.uberapp.UberApp.strategies.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.entities.RideRequest;
import com.example.uberapp.UberApp.services.DistanceService;
import com.example.uberapp.UberApp.strategies.RideFareCalculationStrategy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Primary
public class RideFareDefaultRideFareCalculationStrategy implements RideFareCalculationStrategy{

    private final DistanceService distanceService;

    @Override
    public double calculateFare(RideRequest rideRequest) {
        double distance = distanceService.calculateDistance(rideRequest.getPickupLocation(), rideRequest.getDropoffLocation());
        return distance * rideFareMultiplier;
    }

}

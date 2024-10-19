package com.example.uberapp.UberApp.strategies;

import com.example.uberapp.UberApp.entities.RideRequest;

public interface RideFareCalculationStrategy {

    final double rideFareMultiplier = 10.00;
    public double calculateFare(RideRequest rideRequest);
}

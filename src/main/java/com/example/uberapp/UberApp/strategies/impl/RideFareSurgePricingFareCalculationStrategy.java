package com.example.uberapp.UberApp.strategies.impl;

import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.entities.RideRequest;
import com.example.uberapp.UberApp.strategies.RideFareCalculationStrategy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy{@Override
    public double calculateFare(RideRequest rideRequest) {
        return 0.00;
    }

}

package com.example.uberapp.UberApp.strategies.impl;

import java.util.List;

import org.springframework.stereotype.Service;


import com.example.uberapp.UberApp.entities.Driver;
import com.example.uberapp.UberApp.entities.RideRequest;
import com.example.uberapp.UberApp.repositories.DriverRepository;
import com.example.uberapp.UberApp.strategies.DriverMatchingStrategy;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy{

    private final DriverRepository driverRepository;

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        return driverRepository.findMatchingDriver(rideRequest.getPickupLocation());
    }

}

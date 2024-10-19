package com.example.uberapp.UberApp.strategies;

import java.util.List;


import com.example.uberapp.UberApp.entities.Driver;
import com.example.uberapp.UberApp.entities.RideRequest;

public interface DriverMatchingStrategy {   

    List<Driver> findMatchingDriver(RideRequest rideRequest);
}

package com.example.uberapp.UberApp.services;

import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.dto.DriverDto;
import com.example.uberapp.UberApp.dto.RiderDto;
import com.example.uberapp.UberApp.entities.Ride;

@Service
public interface RatingService {

    DriverDto rateDriver(Ride ride, Double rating);
    RiderDto rateRider(Ride ride, Double rating);
    void createNewRating(Ride ride);
}

package com.example.uberapp.UberApp.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.dto.DriverDto;
import com.example.uberapp.UberApp.dto.RideDto;
import com.example.uberapp.UberApp.dto.RiderDto;
import com.example.uberapp.UberApp.entities.Driver;

@Service
public interface DriverService {

        public RideDto acceptRide(Long rideRequestId);
        public RideDto startRide(Long rideId, String otp);
        public RideDto cancelRide(Long rideId);
        public DriverDto getMyProfile();
        public Page<RideDto> getAllMyRides(PageRequest pageRequest);
        public void updateDriverAvailability(Driver driver, Boolean available);
        public RideDto endRide(Long rideId);
        public RiderDto rateRider(Long rideId, Double rating);
        public Driver createNewDriver(Driver driver);
}

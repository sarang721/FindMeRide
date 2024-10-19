package com.example.uberapp.UberApp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.dto.RideDto;
import com.example.uberapp.UberApp.entities.Driver;
import com.example.uberapp.UberApp.entities.Ride;
import com.example.uberapp.UberApp.entities.RideRequest;
import com.example.uberapp.UberApp.entities.Rider;
import com.example.uberapp.UberApp.entities.enums.RideStatus;

@Service
public interface RideService {

    Ride getRideByID(Long rideId);

    void matchWithDrivers();

    Ride createNewRide(RideRequest rideRequest, Driver driver);

    Ride updateRideStatus(Ride ride, RideStatus status);

    Page<Ride> getAllRideOfDriver(Driver driver, PageRequest pageRequest);

    Page<Ride> getAllRideOfRider(Rider rider, PageRequest pageRequest);

    public Ride update(Ride ride);

}

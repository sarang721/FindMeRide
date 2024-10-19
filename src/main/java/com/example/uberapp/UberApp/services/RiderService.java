package com.example.uberapp.UberApp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.dto.DriverDto;
import com.example.uberapp.UberApp.dto.RideDto;
import com.example.uberapp.UberApp.dto.RideRequestDto;
import com.example.uberapp.UberApp.dto.RiderDto;
import com.example.uberapp.UberApp.entities.Rider;
import com.example.uberapp.UberApp.entities.User;

@Service
public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    Rider createNewRider(User user);

    Rider getCurrentRider();

    RideDto cancelRide(Long rideId);

    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    DriverDto rateDriver(Long rideId, Double rating);

    RiderDto getMyProfile();

}

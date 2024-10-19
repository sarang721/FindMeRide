package com.example.uberapp.UberApp.services.impl;

import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.entities.RideRequest;
import com.example.uberapp.UberApp.entities.enums.RideRequestStatus;
import com.example.uberapp.UberApp.exceptions.ResourceNotFoundException;
import com.example.uberapp.UberApp.repositories.RideRequestRepository;
import com.example.uberapp.UberApp.services.RideRequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RideRequestImpl implements RideRequestService{

    private final RideRequestRepository rideRequestRepository;

    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return  rideRequestRepository.findById(rideRequestId)
        .orElseThrow(()-> new ResourceNotFoundException("RideRequest not found"));
    }

    @Override
    public void update(RideRequest rideRequest) {
        rideRequestRepository.save(rideRequest);
    }   

}

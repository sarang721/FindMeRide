package com.example.uberapp.UberApp.services;

import com.example.uberapp.UberApp.entities.RideRequest;

public interface RideRequestService {

    public RideRequest findRideRequestById(Long rideRequestId);

    public void update(RideRequest rideRequest);

}

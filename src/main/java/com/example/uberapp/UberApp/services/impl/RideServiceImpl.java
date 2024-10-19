package com.example.uberapp.UberApp.services.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.dto.RideDto;
import com.example.uberapp.UberApp.entities.Driver;
import com.example.uberapp.UberApp.entities.Ride;
import com.example.uberapp.UberApp.entities.RideRequest;
import com.example.uberapp.UberApp.entities.Rider;
import com.example.uberapp.UberApp.entities.enums.RideRequestStatus;
import com.example.uberapp.UberApp.entities.enums.RideStatus;
import com.example.uberapp.UberApp.exceptions.ResourceNotFoundException;
import com.example.uberapp.UberApp.repositories.RideRepository;
import com.example.uberapp.UberApp.services.RideRequestService;
import com.example.uberapp.UberApp.services.RideService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService{

    private final RideRepository rideRepository; 
    private final RideRequestService rideRequestService;   
    private final ModelMapper modelMapper;

    @Override
    public Ride getRideByID(Long rideId) {
        return rideRepository.findById(rideId)
        .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: "+ rideId));
    }

    @Override
    public void matchWithDrivers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'matchWithDrivers'");
    }

    @Override
    @Transactional
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);
        Ride ride = modelMapper.map(rideRequest, Ride.class);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setId(null);
        ride.setOtp(generateOtp());
        ride.setDriver(driver);
        rideRequestService.update(rideRequest);
        Ride savedRide = rideRepository.save(ride);
        System.out.println(savedRide);
        return savedRide;
    }

    public Ride update(Ride ride){
        return rideRepository.save(ride);
    }   

    public Ride updateRideStatus(Ride ride, RideStatus status){
        ride.setRideStatus(status);
        return rideRepository.save(ride);
    }

    public String generateOtp(){
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000); 
        return String.valueOf(otp);
    }

    @Override
    public Page<Ride> getAllRideOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver, pageRequest);
    }

    public Page<Ride> getAllRideOfRider(Rider rider, PageRequest pageRequest){
        return rideRepository.findByRider(rider, pageRequest);
    }

}

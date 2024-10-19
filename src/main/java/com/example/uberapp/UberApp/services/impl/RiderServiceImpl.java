package com.example.uberapp.UberApp.services.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.dto.DriverDto;
import com.example.uberapp.UberApp.dto.RideDto;
import com.example.uberapp.UberApp.dto.RideRequestDto;
import com.example.uberapp.UberApp.dto.RiderDto;
import com.example.uberapp.UberApp.entities.Driver;
import com.example.uberapp.UberApp.entities.Ride;
import com.example.uberapp.UberApp.entities.RideRequest;
import com.example.uberapp.UberApp.entities.Rider;
import com.example.uberapp.UberApp.entities.User;
import com.example.uberapp.UberApp.entities.enums.RideRequestStatus;
import com.example.uberapp.UberApp.entities.enums.RideStatus;
import com.example.uberapp.UberApp.exceptions.ResourceNotFoundException;
import com.example.uberapp.UberApp.exceptions.RuntimeConflictException;
import com.example.uberapp.UberApp.repositories.RideRequestRepository;
import com.example.uberapp.UberApp.repositories.RiderRepository;
import com.example.uberapp.UberApp.services.DriverService;
import com.example.uberapp.UberApp.services.RatingService;
import com.example.uberapp.UberApp.services.RideService;
import com.example.uberapp.UberApp.services.RiderService;
import com.example.uberapp.UberApp.strategies.DriverMatchingStrategy;
import com.example.uberapp.UberApp.strategies.RideFareCalculationStrategy;
import com.example.uberapp.UberApp.strategies.RideStrategyManager;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService{

    private final ModelMapper modelMapper;
    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;
    private final RideService rideService;
    private final DriverService driverService;
    private final RatingService ratingService;

    @Override
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        Rider rider = getCurrentRider();
        // model mapper used to convert Dto to entity
        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare); 
        rideRequest.setRider(rider); 
        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);
        // List<Driver> drivers = rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(savedRideRequest);
        // System.out.println(drivers);

        // notify all the drivers 
        return modelMapper.map(savedRideRequest, RideRequestDto.class);
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider.builder().user(user).Rating(0.00).build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return riderRepository.findByUser(user).orElseThrow(()->
            new ResourceNotFoundException("Rider not found")
        );
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideByID(rideId);

        if(!ride.getRider().equals(rider)){
            throw new RuntimeException("Rider does not own this ride");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride cannot be cancelled as it is not confirmed");
        }

        Ride updatedRide = rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        driverService.updateDriverAvailability(ride.getDriver(), true);

        return modelMapper.map(updatedRide, RideDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Rider currentRider = getCurrentRider();
        Page<Ride> allRides = rideService.getAllRideOfRider(currentRider, pageRequest);
        return allRides.map(ride-> modelMapper.map(ride, RideDto.class));
    }

    @Override
    public RiderDto getMyProfile()
    {   
        Rider currentRider = getCurrentRider();
        return modelMapper.map(currentRider, RiderDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Double rating) {
       Ride ride = rideService.getRideByID(rideId);
       Rider currentRider = getCurrentRider();

       if(!currentRider.equals(ride.getRider()))
       {
            throw new RuntimeException("Something went wrong");
       }

       if(!ride.getRideStatus().equals(RideStatus.ENDED)) {
        throw new RuntimeException("Ride status is not Ended hence cannot start rating.");
       }

       DriverDto driverDto = ratingService.rateDriver(ride, rating);
       return driverDto;
    }

}

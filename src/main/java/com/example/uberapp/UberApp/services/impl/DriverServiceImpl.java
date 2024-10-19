package com.example.uberapp.UberApp.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.dto.DriverDto;
import com.example.uberapp.UberApp.dto.RideDto;
import com.example.uberapp.UberApp.dto.RiderDto;
import com.example.uberapp.UberApp.entities.Driver;
import com.example.uberapp.UberApp.entities.Payment;
import com.example.uberapp.UberApp.entities.Ride;
import com.example.uberapp.UberApp.entities.RideRequest;
import com.example.uberapp.UberApp.entities.Rider;
import com.example.uberapp.UberApp.entities.User;
import com.example.uberapp.UberApp.entities.enums.RideRequestStatus;
import com.example.uberapp.UberApp.entities.enums.RideStatus;
import com.example.uberapp.UberApp.exceptions.ResourceNotFoundException;
import com.example.uberapp.UberApp.repositories.DriverRepository;
import com.example.uberapp.UberApp.services.DriverService;
import com.example.uberapp.UberApp.services.PaymentService;
import com.example.uberapp.UberApp.services.RatingService;
import com.example.uberapp.UberApp.services.RideRequestService;
import com.example.uberapp.UberApp.services.RideService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService{

    private final DriverRepository driverRepository;
    private final RideRequestService rideRequestService;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;

    @Override
    public RideDto acceptRide(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);
        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)) {
            throw new RuntimeException("RideRequest cannot be accepted, status is "+ rideRequest.getRideRequestStatus());
        }

        Driver currentDriver = getCurrentDriver();
        if(!currentDriver.getAvailable()){
            throw new ResourceNotFoundException("Driver not available");
        }
        currentDriver.setAvailable(false);
        Driver savedDriver = driverRepository.save(currentDriver);
        
        Ride newRide = rideService.createNewRide(rideRequest, savedDriver);
        return modelMapper.map(newRide, RideDto.class);
    }

    public RideDto startRide(Long rideId, String otp){
        Ride ride = rideService.getRideByID(rideId);
        Driver driver = getCurrentDriver();
        if(!ride.getDriver().getId().equals(driver.getId())){
            throw new RuntimeException("Driver cannot start ride as he has not accepted earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Cannot start ride");
        }

        if(!ride.getOtp().equals(otp)){
            throw new RuntimeException("Otp is not correct");
        }

        driver.setAvailable(false);
        driverRepository.save(driver);
        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);
        
        paymentService.createNewPayment(savedRide);
        ratingService.createNewRating(savedRide);

        return modelMapper.map(savedRide, RideDto.class);
    }

    public Driver getCurrentDriver(){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return driverRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Driver not found"));
    }

    @Override
    public RideDto cancelRide(Long rideId) {

        Driver currentDriver = getCurrentDriver();
        Ride ride = rideService.getRideByID(rideId);

        if(!currentDriver.equals(ride.getDriver())){
            throw new RuntimeException("Cannot cancel ride");
        } 
        
        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride Cannot be cancelled as it is not confirmed");
        }

        ride.setRideStatus(RideStatus.CANCELLED);
        currentDriver.setAvailable(true);
        driverRepository.save(currentDriver);
        rideService.update(ride);

        return modelMapper.map(rideService, RideDto.class);
    }

    @Override
    public DriverDto getMyProfile() {
        Driver currentDriver = getCurrentDriver();
        return modelMapper.map(currentDriver, DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
        return rideService.getAllRideOfDriver(currentDriver, pageRequest).map(ride-> modelMapper.map(ride, RideDto.class));
    }

    @Override
    public void updateDriverAvailability(Driver driver, Boolean available) {
        driver.setAvailable(available);
        driverRepository.save(driver);
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {
        Ride ride = rideService.getRideByID(rideId);
        Driver driver = getCurrentDriver();
        if(!ride.getDriver().getId().equals(driver.getId())){
            throw new RuntimeException("Driver cannot end ride as he has not accepted earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.ONGOING)){
            throw new RuntimeException("Cannot end ride");
        }

        ride.setEndedAt(LocalDateTime.now());
        rideService.updateRideStatus(ride, RideStatus.ENDED);
        updateDriverAvailability(driver, true);
        paymentService.processPayment(ride);
        return modelMapper.map(ride, RideDto.class);
        
    }

    @Override
    public RiderDto rateRider(Long rideId, Double rating) {
       Ride ride = rideService.getRideByID(rideId);
       Driver currentDriver = getCurrentDriver();

       if(!currentDriver.equals(ride.getDriver()))
       {
            throw new RuntimeException("Something went wrong");
       }

       if(!ride.getRideStatus().equals(RideStatus.ENDED)) {
        throw new RuntimeException("Ride status is not Ended hence cannot start rating.");
       }

       RiderDto riderDto = ratingService.rateRider(ride, rating);
       return riderDto;
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }

}

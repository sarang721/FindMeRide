package com.example.uberapp.UberApp.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.dto.DriverDto;
import com.example.uberapp.UberApp.dto.RiderDto;
import com.example.uberapp.UberApp.entities.Driver;
import com.example.uberapp.UberApp.entities.Rating;
import com.example.uberapp.UberApp.entities.Ride;
import com.example.uberapp.UberApp.entities.Rider;
import com.example.uberapp.UberApp.exceptions.ResourceNotFoundException;
import com.example.uberapp.UberApp.exceptions.RuntimeConflictException;
import com.example.uberapp.UberApp.repositories.DriverRepository;
import com.example.uberapp.UberApp.repositories.RatingRepository;
import com.example.uberapp.UberApp.repositories.RiderRepository;
import com.example.uberapp.UberApp.services.RatingService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService{

    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;

    @Override
    public DriverDto rateDriver(Ride ride, Double rating) {
    
        Driver driver = ride.getDriver();
        Rating ratingObj = ratingRepository.findByDriver(driver).orElseThrow(()->new ResourceNotFoundException("RatingObj not found"));
        
        if(ratingObj.getDriverRating()!=null)
        {
            throw new RuntimeConflictException("Driver has already rated");
        }

        ratingObj.setDriverRating(rating);
        ratingRepository.save(ratingObj);

        Double avgDriverRating = ratingRepository.findByDriver(driver)
        .stream().mapToDouble(rating1 -> rating1.getDriverRating()).average().orElse(0.0);

        driver.setRating(avgDriverRating);
        Driver savedDriver = driverRepository.save(driver);
        return modelMapper.map(savedDriver, DriverDto.class);
    }

    @Override
    public RiderDto rateRider(Ride ride, Double rating) {
        
        Rider rider = ride.getRider();
        Rating ratingObj = ratingRepository.findByRider(rider).orElseThrow(()->new ResourceNotFoundException("RatingObj not found"));

        if(ratingObj.getDriverRating()!=null){
          throw new RuntimeConflictException("Rider already rated");
        }

        ratingObj.setRiderRating(rating);
        ratingRepository.save(ratingObj);

        Double avgRiderRating = ratingRepository.findByRider(rider)
        .stream()
        .mapToDouble(rating1->rating1.getRiderRating())
        .average().orElse(0.0);

        rider.setRating(avgRiderRating);
        Rider savedRider = riderRepository.save(rider);
        return modelMapper.map(savedRider, RiderDto.class);
    }

    @Override
    public void createNewRating(Ride ride) {
        Driver driver = ride.getDriver();
        Rider rider = ride.getRider();
        Rating rating = Rating.builder()
            .ride(ride)
            .driver(driver)
            .rider(rider)
            .build();

            ratingRepository.save(rating);
    }
    
}

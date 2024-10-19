package com.example.uberapp.UberApp.strategies;

import java.time.LocalTime;

import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.strategies.impl.DriverMatchingNearestDriverStrategy;
import com.example.uberapp.UberApp.strategies.impl.RideFareDefaultRideFareCalculationStrategy;
import com.example.uberapp.UberApp.strategies.impl.RideFareSurgePricingFareCalculationStrategy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RideStrategyManager {

        private final RideFareDefaultRideFareCalculationStrategy rideFareDefaultRideFareCalculationStrategy;
        private final RideFareSurgePricingFareCalculationStrategy rideFareSurgePricingFareCalculationStrategy;
        private final DriverMatchingNearestDriverStrategy driverMatchingNearestDriverStrategy;

        public DriverMatchingStrategy driverMatchingStrategy(double riderRating){
            if(riderRating > 4.9){
                return driverMatchingNearestDriverStrategy;
            }  
            return driverMatchingNearestDriverStrategy;
        }

        public RideFareCalculationStrategy rideFareCalculationStrategy(){
            LocalTime surgeStartTime = LocalTime.of(18, 0);
            LocalTime surgeEndTime = LocalTime.of(21, 0);
            LocalTime currentTime = LocalTime.now();
            boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

            if(isSurgeTime){
                return rideFareSurgePricingFareCalculationStrategy;
            }
            else{
                return rideFareDefaultRideFareCalculationStrategy;
            }

        }
}

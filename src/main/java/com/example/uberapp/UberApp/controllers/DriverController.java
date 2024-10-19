package com.example.uberapp.UberApp.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.uberapp.UberApp.dto.DriverDto;
import com.example.uberapp.UberApp.dto.RatingDto;
import com.example.uberapp.UberApp.dto.RideDto;
import com.example.uberapp.UberApp.dto.RiderDto;
import com.example.uberapp.UberApp.services.DriverService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(path = "/driver")
@RequiredArgsConstructor
@Secured("ROLE_DRIVER")
public class DriverController {

    private final DriverService driverService;

    @PostMapping("/acceptRide/{rideRequestId}")
    public RideDto acceptRideFn(@PathVariable Long rideRequestId){
            return driverService.acceptRide(rideRequestId);
    }
    
    @PostMapping("/startRide/{rideId}/{otp}")
    public RideDto startRideFn(@PathVariable Long rideId,@PathVariable String otp){
        return driverService.startRide(rideId, otp);
    }

    @GetMapping("/getMyProfile")
    public DriverDto getDriverProfile() {
        return driverService.getMyProfile();
    }

    @GetMapping("/endRide/{rideId}")
    public RideDto endRide(@RequestParam Long rideId) {
        return driverService.endRide(rideId);
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDto> cancelRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(driverService.cancelRide(rideId));
    }

    @PostMapping("/rateRider")
    public ResponseEntity<RiderDto> rateRider(@RequestBody RatingDto ratingDto) {
        return ResponseEntity.ok(driverService.rateRider(ratingDto.getRideId(), ratingDto.getRating()));
    }

    @GetMapping("/getMyRides")
    public ResponseEntity<Page<RideDto>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                       @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.DESC, "createdTime", "id"));
        return ResponseEntity.ok(driverService.getAllMyRides(pageRequest));
    }
}

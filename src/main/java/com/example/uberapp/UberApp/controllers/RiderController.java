package com.example.uberapp.UberApp.controllers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.uberapp.UberApp.dto.DriverDto;
import com.example.uberapp.UberApp.dto.RatingDto;
import com.example.uberapp.UberApp.dto.RideDto;
import com.example.uberapp.UberApp.dto.RideRequestDto;
import com.example.uberapp.UberApp.dto.RiderDto;
import com.example.uberapp.UberApp.services.RiderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/rider")
@RequiredArgsConstructor
@Secured("ROLE_RIDER")
public class RiderController {

    private final RiderService riderService;

    @PostMapping("/requestRide")
    public RideRequestDto requestRide(@RequestBody RideRequestDto rideRequestDto) {
        return riderService.requestRide(rideRequestDto);
    }  

    @PostMapping("/cancelRide/{rideId}")
    public RideDto cancelRide(@PathVariable Long rideId){
        return riderService.cancelRide(rideId);
    }

    @PostMapping("/rateDriver")
    public ResponseEntity<DriverDto> rateDriver(@RequestBody RatingDto ratingDto) {
        return ResponseEntity.ok(riderService.rateDriver(ratingDto.getRideId(), ratingDto.getRating()));
    }

    @GetMapping("/getMyprofile")
    public ResponseEntity<RiderDto> getMyProfile(){
        return ResponseEntity.ok(riderService.getMyProfile());
    }

    @GetMapping("/getMyRides")
    public ResponseEntity<Page<RideDto>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                       @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.DESC, "createdTime", "id"));
        return ResponseEntity.ok(riderService.getAllMyRides(pageRequest));
    }
}

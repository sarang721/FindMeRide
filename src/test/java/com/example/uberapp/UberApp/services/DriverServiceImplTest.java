package com.example.uberapp.UberApp.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.uberapp.UberApp.dto.RideDto;
import com.example.uberapp.UberApp.entities.Driver;
import com.example.uberapp.UberApp.entities.Ride;
import com.example.uberapp.UberApp.entities.RideRequest;
import com.example.uberapp.UberApp.entities.User;
import com.example.uberapp.UberApp.entities.enums.PaymentMethod;
import com.example.uberapp.UberApp.entities.enums.RideRequestStatus;
import com.example.uberapp.UberApp.repositories.DriverRepository;
import com.example.uberapp.UberApp.services.impl.DriverServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DriverServiceImplTest {

    @Mock
    private  DriverRepository driverRepository;
    @Mock
    private  RideRequestService rideRequestService;
    @Mock
    private  RideService rideService;
    @Spy
    private  ModelMapper modelMapper;
    @Mock
    private  PaymentService paymentService;
    @Mock
    private  RatingService ratingService;

    @InjectMocks
    private DriverServiceImpl driverServiceImpl;

    private RideRequest rideRequest;
    private User user;
    private Driver driver;
    private Ride ride;

    @BeforeEach
    void setup(){
        rideRequest = new RideRequest();
        rideRequest.setRider(null);
        rideRequest.setFare(0.0);
        rideRequest.setPaymentMethod(PaymentMethod.CASH);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);

        user=new User();
        user.setEmail("abc@gmail.com");
        user.setId(1L);
        user.setPassword("password");
        user.setName("test");
        user.setRoles(new HashSet<>());

        driver = Driver.builder()
                .user(user)
                .rating(0.0)
                .available(true)
                .vehicleId("vehicleid")
                .build();

        ride = new Ride();
        ride.setDriver(driver);
        ride.setRider(null);
    }

    @Test
    void test_acceptRide_success(){

        when(rideRequestService.findRideRequestById(anyLong())).thenReturn(rideRequest);
        
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        when(driverRepository.findByUser(any(User.class))).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);
        when(rideService.createNewRide(any(RideRequest.class), any(Driver.class))).thenReturn(ride);

        RideDto rideRequest = driverServiceImpl.acceptRide(1L);

        ArgumentCaptor<Driver> driverCaptor = ArgumentCaptor.forClass(Driver.class);
        verify(driverRepository).save(driverCaptor.capture());

        Driver capturedDriver = driverCaptor.getValue();
        assertThat(capturedDriver.getUser().getName()).isEqualTo(user.getName());

        assertThat(rideRequest.getDriver().getVehicleId()).isEqualTo(driver.getVehicleId());
    }
    
}

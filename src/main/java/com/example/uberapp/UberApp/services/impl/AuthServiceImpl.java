package com.example.uberapp.UberApp.services.impl;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.dto.DriverDto;
import com.example.uberapp.UberApp.dto.SignupDto;
import com.example.uberapp.UberApp.dto.UserDto;
import com.example.uberapp.UberApp.entities.Driver;
import com.example.uberapp.UberApp.entities.User;
import com.example.uberapp.UberApp.entities.enums.Role;
import com.example.uberapp.UberApp.exceptions.ResourceNotFoundException;
import com.example.uberapp.UberApp.exceptions.RuntimeConflictException;
import com.example.uberapp.UberApp.repositories.UserRepository;
import com.example.uberapp.UberApp.security.JWTService;
import com.example.uberapp.UberApp.services.AuthService;
import com.example.uberapp.UberApp.services.DriverService;
import com.example.uberapp.UberApp.services.RiderService;
import com.example.uberapp.UberApp.services.WalletService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public String[] login(String email, String password) {
        String tokens[] = new String[2];

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        tokens[0]=accessToken;
        tokens[1]=refreshToken;
        return tokens;
    }

    @Override
    @Transactional
    public UserDto signUp(SignupDto signupDto) {
        User user = userRepository.findByEmail(signupDto.getEmail()).orElse(null);
        if(user !=null){
            throw new RuntimeConflictException("User already exists");
        }
        User mappedUser = modelMapper.map(signupDto, User.class);
        Set<Role> roles = Set.of(Role.RIDER);
        mappedUser.setRoles(roles);
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
        User savedUser = userRepository.save(mappedUser);
        riderService.createNewRider(savedUser);
        walletService.createNewWallet(savedUser);
       return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public DriverDto onboardNewDriver(Long userId, String vehicleId) {
        
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        if(user.getRoles().contains(Role.DRIVER)){
            throw new RuntimeConflictException("User is already a driver");
        }

        user.getRoles().add(Role.DRIVER);
        userRepository.save(user);

        Driver driver = Driver.builder()
            .user(user)
            .rating(0.0)
            .available(true)
            .vehicleId(vehicleId)
            .build();

        Driver savedDriver = driverService.createNewDriver(driver);
        return modelMapper.map(savedDriver, DriverDto.class);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found " +
                "with id: "+userId));

        return jwtService.generateAccessToken(user);
    }

}

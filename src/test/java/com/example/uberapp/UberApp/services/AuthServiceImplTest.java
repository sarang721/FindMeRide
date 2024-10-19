package com.example.uberapp.UberApp.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

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
import com.example.uberapp.UberApp.services.impl.AuthServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authServiceImpl;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  RiderService riderService;
    @Mock
    private  WalletService walletService;
    @Mock
    private  DriverService driverService;
    @Spy
    private  PasswordEncoder passwordEncoder;
    @Spy
    private  AuthenticationManager authenticationManager;
    @Mock
    private  JWTService jwtService;


    private User user;
    private Driver driver;
    private DriverDto driverDto;

    @BeforeEach
    void setUp(){
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

        driverDto = modelMapper.map(driver, DriverDto.class);

    }

    @Test
    void testLogin_whenSuccess(){

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        when(jwtService.generateAccessToken(any(User.class))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        String[] tokens = authServiceImpl.login(user.getEmail(), user.getPassword());

        assertThat(tokens[0]).isEqualTo("accessToken");
        assertThat(tokens[1]).isEqualTo("refreshToken");
        

    }

    @Test   
    void testSignup_failed(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        SignupDto signupDto = new SignupDto();
        signupDto.setEmail("test@gmail.com");
        signupDto.setName("test");
        signupDto.setPassword( "password");
        
        assertThatThrownBy(() -> authServiceImpl.signUp(signupDto))
                .isInstanceOf(RuntimeConflictException.class)
                .hasMessage("User already exists");
                

        verify(userRepository).findByEmail(anyString());
    }

    @Test
    void testSignup_whenSuccess(){

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        SignupDto signupDto = new SignupDto();
        signupDto.setEmail("test@gmail.com");
        signupDto.setName("test");
        signupDto.setPassword("password");

        UserDto userDto = authServiceImpl.signUp(signupDto);
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getName()).isEqualTo(user.getName());

    }

    @Test
    void testRefreshToken_success(){

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("accessToken");

        String accessToken = authServiceImpl.refreshToken("refreshtoken");
        assertThat(accessToken).isEqualTo("accessToken");

    }

    @Test
    void testOnboardNewDriver_success(){

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(driverService.createNewDriver(any(Driver.class))).thenReturn(driver);

        DriverDto savedDriver = authServiceImpl.onboardNewDriver(1L, "vehicleid");
        assertThat(savedDriver.getId()).isEqualTo(driver.getId());
        assertThat(savedDriver.getAvailable()).isTrue();
        assertThat(savedDriver.getUser().getName()).isEqualTo(user.getName());

    }

    @Test
    void test_onBoardNewDriver_failed(){
        user.getRoles().add(Role.DRIVER);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertThatThrownBy(() -> authServiceImpl.onboardNewDriver(1L, "vehicleid"))
        .isInstanceOf(RuntimeConflictException.class)
        .hasMessage("User is already a driver");

    }

}

package com.example.uberapp.UberApp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uberapp.UberApp.dto.DriverDto;
import com.example.uberapp.UberApp.dto.LoginRequestDto;
import com.example.uberapp.UberApp.dto.LoginResponseDto;
import com.example.uberapp.UberApp.dto.OnboardDriverDto;
import com.example.uberapp.UberApp.dto.SignupDto;
import com.example.uberapp.UberApp.dto.UserDto;
import com.example.uberapp.UberApp.services.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    ResponseEntity<UserDto> signup(@RequestBody SignupDto signupDto){
        return new ResponseEntity<>(authService.signUp(signupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/onBoardNewDriver/{userId}")
    ResponseEntity<DriverDto> onBoardNewDriver(@PathVariable Long userId, @RequestBody OnboardDriverDto onboardDriverDto) {
        return new ResponseEntity<>(authService.onboardNewDriver(userId,
                onboardDriverDto.getVehicleId()), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        String tokens[] = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        Cookie cookie = new Cookie("token", tokens[1]);
        cookie.setHttpOnly(true);

        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok(new LoginResponseDto(tokens[0]));
    }

    @PostMapping("/refresh")
    ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if(refreshToken == null){
            throw new AuthenticationServiceException("Refresh token not found");
        }

        String accessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new LoginResponseDto(accessToken));
    }
    
    


}

package com.example.uberapp.UberApp.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;
import org.springframework.cglib.core.Local;

import com.example.uberapp.UberApp.entities.enums.PaymentMethod;
import com.example.uberapp.UberApp.entities.enums.RideStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rider rider;

    @ManyToOne(fetch = FetchType.LAZY)
    private Driver driver;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point pickupLocation;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point dropoffLocation;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus;

    private Double fare;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private String otp;

}

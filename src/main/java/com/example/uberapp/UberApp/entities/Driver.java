package com.example.uberapp.UberApp.entities;


import org.locationtech.jts.geom.Point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double rating;

    private Boolean available;

    private String vehicleId;
    
    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point currentLocation;

}

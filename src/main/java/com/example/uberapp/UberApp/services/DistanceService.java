package com.example.uberapp.UberApp.services;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public interface DistanceService {

    double calculateDistance(Point src, Point dst);
} 

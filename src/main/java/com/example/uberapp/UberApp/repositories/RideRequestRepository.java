package com.example.uberapp.UberApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.uberapp.UberApp.entities.RideRequest;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {
        
}




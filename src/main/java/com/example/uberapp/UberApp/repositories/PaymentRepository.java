package com.example.uberapp.UberApp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.uberapp.UberApp.entities.Payment;
import com.example.uberapp.UberApp.entities.Ride;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRide(Ride ride);
    
}

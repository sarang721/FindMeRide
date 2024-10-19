package com.example.uberapp.UberApp.services;

import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.entities.Payment;
import com.example.uberapp.UberApp.entities.Ride;
import com.example.uberapp.UberApp.entities.enums.PaymentStatus;

@Service
public interface PaymentService {

    public void processPayment(Ride ride);

    public Payment createNewPayment(Ride ride);

    public void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus);
    
}

package com.example.uberapp.UberApp.strategies;

import com.example.uberapp.UberApp.entities.Payment;

public interface PaymentStrategy {
    
    void processPayment(Payment payment);
}

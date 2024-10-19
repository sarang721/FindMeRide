package com.example.uberapp.UberApp.services.impl;

import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.entities.Payment;
import com.example.uberapp.UberApp.entities.Ride;
import com.example.uberapp.UberApp.entities.enums.PaymentStatus;
import com.example.uberapp.UberApp.exceptions.ResourceNotFoundException;
import com.example.uberapp.UberApp.repositories.PaymentRepository;
import com.example.uberapp.UberApp.services.PaymentService;
import com.example.uberapp.UberApp.strategies.PaymentStrategyManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride) {
        Payment payment = paymentRepository.findByRide(ride).orElseThrow(()-> new ResourceNotFoundException("Payment Obj not found"));
        paymentStrategyManager.paymentStrategy(payment.getPaymentMethod()).processPayment(payment);
    }

    @Override
    public Payment createNewPayment(Ride ride) {
        Payment payment = Payment.builder()
        .ride(ride)
        .paymentMethod(ride.getPaymentMethod())
        .amount(ride.getFare())
        .paymentStatus(PaymentStatus.PENDING)
        .build();

        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus) {
        payment.setPaymentStatus(paymentStatus);
        paymentRepository.save(payment);
    }

    
}

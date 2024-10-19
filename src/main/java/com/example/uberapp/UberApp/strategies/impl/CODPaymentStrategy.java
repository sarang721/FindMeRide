package com.example.uberapp.UberApp.strategies.impl;

import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.entities.Driver;
import com.example.uberapp.UberApp.entities.Payment;
import com.example.uberapp.UberApp.entities.Wallet;
import com.example.uberapp.UberApp.entities.enums.PaymentStatus;
import com.example.uberapp.UberApp.entities.enums.TransactionMethod;
import com.example.uberapp.UberApp.repositories.PaymentRepository;
import com.example.uberapp.UberApp.services.PaymentService;
import com.example.uberapp.UberApp.services.WalletService;
import com.example.uberapp.UberApp.strategies.PaymentStrategy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CODPaymentStrategy implements PaymentStrategy{

    private final WalletService walletService;
    // private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Double platformCommission = payment.getAmount()*0.3;
        walletService.deductMoneyFromWallet(driver.getUser(), platformCommission, null, payment.getRide(), TransactionMethod.RIDE);
        // paymentService.updatePaymentStatus(payment, PaymentStatus.CONFIRMED);  give circular dependency so removed
        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    
    }   
}

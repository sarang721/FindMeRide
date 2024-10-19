package com.example.uberapp.UberApp.strategies.impl;

import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.entities.Driver;
import com.example.uberapp.UberApp.entities.Payment;
import com.example.uberapp.UberApp.entities.Rider;
import com.example.uberapp.UberApp.entities.enums.PaymentStatus;
import com.example.uberapp.UberApp.entities.enums.TransactionMethod;
import com.example.uberapp.UberApp.repositories.PaymentRepository;
import com.example.uberapp.UberApp.services.PaymentService;
import com.example.uberapp.UberApp.services.WalletService;
import com.example.uberapp.UberApp.strategies.PaymentStrategy;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy{

    private final WalletService walletService;
    // private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void processPayment(Payment payment) {
        Rider rider = payment.getRide().getRider();
        Driver driver = payment.getRide().getDriver();

        walletService.deductMoneyFromWallet(rider.getUser(), payment.getAmount(), null, payment.getRide(), TransactionMethod.RIDE);
        Double driversCut = payment.getAmount() - 30;
        walletService.addMoneyToWallet(driver.getUser(), driversCut, null, payment.getRide(), TransactionMethod.RIDE);
        // paymentService.updatePaymentStatus(payment, PaymentStatus.CONFIRMED);
        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    
    }
    
}

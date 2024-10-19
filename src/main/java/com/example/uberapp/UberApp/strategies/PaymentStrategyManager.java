package com.example.uberapp.UberApp.strategies;

import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.entities.enums.PaymentMethod;
import com.example.uberapp.UberApp.strategies.impl.CODPaymentStrategy;
import com.example.uberapp.UberApp.strategies.impl.WalletPaymentStrategy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final CODPaymentStrategy codPaymentStrategy;
    private final WalletPaymentStrategy walletPaymentStrategy;


    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod)
    {
        if(paymentMethod.equals(PaymentMethod.WALLET))
        {
            return walletPaymentStrategy;
        }
        else if(paymentMethod.equals(PaymentMethod.CASH))
        {
            return codPaymentStrategy;
        }
        throw new RuntimeException("Invalid payment method");
    }

    
}

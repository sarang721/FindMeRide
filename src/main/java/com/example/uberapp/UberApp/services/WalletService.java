package com.example.uberapp.UberApp.services;

import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.entities.Ride;
import com.example.uberapp.UberApp.entities.User;
import com.example.uberapp.UberApp.entities.Wallet;
import com.example.uberapp.UberApp.entities.enums.TransactionMethod;

@Service
public interface WalletService {
    
    Wallet addMoneyToWallet(User user, Double amount, String txnId, Ride ride, TransactionMethod txnMethod);

    Wallet deductMoneyFromWallet(User user, Double amount, String txnId, Ride ride, TransactionMethod txnMethod);

    void withdrawMoneyFromWallet();

    Wallet findWalletById(Long id);

    Wallet createNewWallet(User user);

    Wallet findByUser(User user);

}

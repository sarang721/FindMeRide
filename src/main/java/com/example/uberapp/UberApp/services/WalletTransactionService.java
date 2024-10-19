package com.example.uberapp.UberApp.services;

import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.dto.WalletTransactionDto;
import com.example.uberapp.UberApp.entities.WalletTransaction;


@Service
public interface WalletTransactionService {
    void createNewWalletTransaction(WalletTransaction walletTransaction);
}

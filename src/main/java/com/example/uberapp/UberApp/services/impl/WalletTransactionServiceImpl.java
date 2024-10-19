package com.example.uberapp.UberApp.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.dto.WalletTransactionDto;
import com.example.uberapp.UberApp.entities.WalletTransaction;
import com.example.uberapp.UberApp.repositories.WalletRepository;
import com.example.uberapp.UberApp.repositories.WalletTransactionRepository;
import com.example.uberapp.UberApp.services.WalletTransactionService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {
    
    private final WalletTransactionRepository walletTransactionRepository;
    
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        // WalletTransaction walletTransaction = modelMapper.map(walletTransactionDto, WalletTransaction.class);
        walletTransactionRepository.save(walletTransaction);
    }
    
}

package com.example.uberapp.UberApp.services.impl;

import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.entities.Ride;
import com.example.uberapp.UberApp.entities.User;
import com.example.uberapp.UberApp.entities.Wallet;
import com.example.uberapp.UberApp.entities.WalletTransaction;
import com.example.uberapp.UberApp.entities.enums.TransactionMethod;
import com.example.uberapp.UberApp.entities.enums.TransactionType;
import com.example.uberapp.UberApp.exceptions.ResourceNotFoundException;
import com.example.uberapp.UberApp.repositories.WalletRepository;
import com.example.uberapp.UberApp.services.WalletService;
import com.example.uberapp.UberApp.services.WalletTransactionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService{

    private final WalletRepository walletRepository;
    private final WalletTransactionService walletTransactionService;

    @Override
    @Transactional
    public Wallet addMoneyToWallet(User user, Double amount, String txnId, Ride ride, TransactionMethod txnMethod) {

        Wallet wallet = walletRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Wallet not found"));
        wallet.setBalance(wallet.getBalance()+amount);

        WalletTransaction walletTxn = WalletTransaction.builder().
            transactionId(txnId)
            .ride(ride)
            .wallet(wallet)
            .transactionType(TransactionType.CREDIT)
            .transactionMethod(txnMethod)
            .amount(amount)
            .build();
        
        walletTransactionService.createNewWalletTransaction(walletTxn);

        return walletRepository.save(wallet);
    }   

    @Override
    public void withdrawMoneyFromWallet() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdrawMoneyFromWallet'");
    }

    @Override
    public Wallet findWalletById(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Wallet not found"));
        return wallet;
    }

    @Override
    public Wallet createNewWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(0.0);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        Wallet wallet = walletRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("Wallet not found for user"));
        return wallet;
    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, Double amount, String txnId, Ride ride, TransactionMethod txnMethod) {
        Wallet wallet = walletRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Wallet not found"));
        wallet.setBalance(wallet.getBalance()-amount);
        WalletTransaction walletTxn = WalletTransaction.builder().
            transactionId(txnId)
            .ride(ride)
            .wallet(wallet)
            .transactionType(TransactionType.DEBIT)
            .transactionMethod(txnMethod)
            .amount(amount)
            .build();

        wallet.getTransactions().add(walletTxn);
        return walletRepository.save(wallet);
    }
    
}

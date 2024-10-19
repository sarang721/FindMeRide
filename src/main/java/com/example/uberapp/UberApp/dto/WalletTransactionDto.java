package com.example.uberapp.UberApp.dto;

import java.time.LocalDateTime;

import com.example.uberapp.UberApp.entities.enums.TransactionMethod;
import com.example.uberapp.UberApp.entities.enums.TransactionType;

import lombok.*;

@Data
@Builder
public class WalletTransactionDto {

    private Long id;

    private Double amount;

    private TransactionType transactionType;

    private TransactionMethod transactionMethod;

    private RideDto ride;

    private String transactionId;

    private WalletDto wallet;

    private LocalDateTime timeStamp;

}

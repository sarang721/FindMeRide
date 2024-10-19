package com.example.uberapp.UberApp.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.uberapp.UberApp.entities.enums.TransactionMethod;
import com.example.uberapp.UberApp.entities.enums.TransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;

    private Double amount;

    private TransactionType transactionType;

    private TransactionMethod transactionMethod;

    @ManyToOne
    private Ride ride;

    private String transactionId;

    @ManyToOne
    private Wallet wallet;

    @CreationTimestamp
    private LocalDateTime timeStamp;

}

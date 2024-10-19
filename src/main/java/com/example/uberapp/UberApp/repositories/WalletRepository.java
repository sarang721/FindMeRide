package com.example.uberapp.UberApp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.uberapp.UberApp.entities.User;
import com.example.uberapp.UberApp.entities.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>{
    
    Optional<Wallet> findByUser(User user);
}

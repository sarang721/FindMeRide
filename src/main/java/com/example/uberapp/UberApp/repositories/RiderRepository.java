package com.example.uberapp.UberApp.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.uberapp.UberApp.entities.Rider;
import com.example.uberapp.UberApp.entities.User;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {
    
    Optional<Rider> findByUser(User user);
}

package com.example.uberapp.UberApp.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.uberapp.UberApp.entities.Driver;
import com.example.uberapp.UberApp.entities.Rating;
import com.example.uberapp.UberApp.entities.Rider;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>{

    Optional<Rating> findByDriver(Driver driver);
    Optional<Rating> findByRider(Rider rider);
    
}

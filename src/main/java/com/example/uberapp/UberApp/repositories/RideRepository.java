package com.example.uberapp.UberApp.repositories;

import com.example.uberapp.UberApp.entities.Driver;
import com.example.uberapp.UberApp.entities.Ride;
import com.example.uberapp.UberApp.entities.Rider;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long>{

    Page<Ride> findByDriver(Driver driver, Pageable pageRequest);
    Page<Ride> findByRider(Rider rider, Pageable pageRequest);

}

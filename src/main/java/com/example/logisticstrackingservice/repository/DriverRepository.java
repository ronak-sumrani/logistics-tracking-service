package com.example.logisticstrackingservice.repository;

import com.example.logisticstrackingservice.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByMobile(String mobile);
}

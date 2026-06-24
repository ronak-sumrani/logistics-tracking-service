package com.example.logisticstrackingservice.repository;

import com.example.logisticstrackingservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}

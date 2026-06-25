package com.example.logisticstrackingservice.service;

import com.example.logisticstrackingservice.entity.Customer;
import com.example.logisticstrackingservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer findOrCreate(String name, String mobile) {
        return customerRepository.findByMobile(mobile)
                .orElseGet(() -> {
                    Customer customer = new Customer();
                    customer.setName(name);
                    customer.setMobile(mobile);
                    return customerRepository.save(customer);
                });
    }
}

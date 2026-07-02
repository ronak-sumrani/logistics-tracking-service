package com.example.logisticstrackingservice.service;

import com.example.logisticstrackingservice.dto.response.CustomerResponse;
import com.example.logisticstrackingservice.entity.Customer;
import com.example.logisticstrackingservice.mapper.CustomerMapper;
import com.example.logisticstrackingservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public Customer findOrCreate(String name, String mobile) {
        return customerRepository.findByMobile(mobile)
                .orElseGet(() -> {
                    Customer customer = new Customer();
                    customer.setName(name);
                    customer.setMobile(mobile);
                    return customerRepository.save(customer);
                });
    }

    public CustomerResponse findOrCreateAndReturn(String name, String mobile) {
        Customer customer = findOrCreate(name, mobile);
        return customerMapper.toResponse(customer);
    }
}

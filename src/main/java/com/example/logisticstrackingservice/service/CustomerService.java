package com.example.logisticstrackingservice.service;

import com.example.logisticstrackingservice.dto.response.CustomerResponse;
import com.example.logisticstrackingservice.entity.Customer;
import com.example.logisticstrackingservice.enums.NotificationChannel;
import com.example.logisticstrackingservice.mapper.CustomerMapper;
import com.example.logisticstrackingservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public Customer findOrCreate(String name, String mobile, List<NotificationChannel> preferredChannels) {
        return customerRepository.findByMobile(mobile)
                .orElseGet(() -> {
                    Customer customer = new Customer();
                    customer.setName(name);
                    customer.setMobile(mobile);
                    customer.setPreferredChannels(
                            (preferredChannels == null || preferredChannels.isEmpty())
                                    ? List.of(NotificationChannel.SMS)
                                    : preferredChannels
                    );
                    return customerRepository.save(customer);
                });
    }

    public CustomerResponse findOrCreateAndReturn(String name, String mobile, List<NotificationChannel> preferredChannels) {
        Customer customer = findOrCreate(name, mobile, preferredChannels);
        return customerMapper.toResponse(customer);
    }
}
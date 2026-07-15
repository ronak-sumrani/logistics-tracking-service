package com.example.logisticstrackingservice.mapper;

import com.example.logisticstrackingservice.dto.response.CustomerResponse;
import com.example.logisticstrackingservice.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setMobile(customer.getMobile());
        response.setPreferredChannels(customer.getPreferredChannels());
        return response;
    }
}
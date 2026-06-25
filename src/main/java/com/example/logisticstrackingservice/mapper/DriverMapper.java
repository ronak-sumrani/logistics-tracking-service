package com.example.logisticstrackingservice.mapper;

import com.example.logisticstrackingservice.dto.request.CreateDriverRequest;
import com.example.logisticstrackingservice.dto.response.DriverResponse;
import com.example.logisticstrackingservice.entity.Driver;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {

    public DriverResponse toResponse(Driver driver) {
        DriverResponse response = new DriverResponse();
        response.setId(driver.getId());
        response.setName(driver.getName());
        response.setMobile(driver.getMobile());
        return response;
    }

    public Driver toEntity(CreateDriverRequest request) {
        Driver driver = new Driver();
        driver.setName(request.getName());
        driver.setMobile(request.getMobile());
        return driver;
    }
}

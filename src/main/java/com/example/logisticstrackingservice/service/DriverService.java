package com.example.logisticstrackingservice.service;

import com.example.logisticstrackingservice.dto.request.CreateDriverRequest;
import com.example.logisticstrackingservice.dto.response.DriverResponse;
import com.example.logisticstrackingservice.entity.Driver;
import com.example.logisticstrackingservice.mapper.DriverMapper;
import com.example.logisticstrackingservice.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverMapper driverMapper;

    @Transactional
    public DriverResponse createDriver(CreateDriverRequest request) {
        if (driverRepository.existsByMobile(request.getMobile())) {
            throw new RuntimeException("Driver already exists with mobile: " + request.getMobile());
        }
        Driver driver = driverMapper.toEntity(request);
        Driver saved = driverRepository.save(driver);
        return driverMapper.toResponse(saved);
    }

    public DriverResponse findById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found for id: " + id));
        return driverMapper.toResponse(driver);
    }
}

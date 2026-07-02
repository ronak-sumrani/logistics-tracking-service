package com.example.logisticstrackingservice.service;

import com.example.logisticstrackingservice.dto.request.CreateDriverRequest;
import com.example.logisticstrackingservice.dto.response.DriverResponse;
import com.example.logisticstrackingservice.entity.Driver;
import com.example.logisticstrackingservice.exception.DriverAlreadyExistsException;
import com.example.logisticstrackingservice.exception.DriverNotFoundException;
import com.example.logisticstrackingservice.mapper.DriverMapper;
import com.example.logisticstrackingservice.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Transactional
    public DriverResponse createDriver(CreateDriverRequest request) {
        if (driverRepository.existsByMobile(request.getMobile())) {
            throw new DriverAlreadyExistsException("Driver already exists with mobile: " + request.getMobile());
        }
        Driver driver = driverMapper.toEntity(request);
        Driver saved = driverRepository.save(driver);
        return driverMapper.toResponse(saved);
    }

    public DriverResponse findById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found for id: " + id));
        return driverMapper.toResponse(driver);
    }
}

package com.example.logisticstrackingservice.repository;

import com.example.logisticstrackingservice.entity.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {
    Optional<Consignment> findByConsignmentNumber(String consignmentNumber);

    @Query("SELECT c FROM Consignment c JOIN FETCH c.receiver WHERE c.id = :id")
    Optional<Consignment> findByIdWithReceiver(@Param("id") Long id);

}

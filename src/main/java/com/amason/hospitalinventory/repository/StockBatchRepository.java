package com.amason.hospitalinventory.repository;

import com.amason.hospitalinventory.model.StockBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface StockBatchRepository extends JpaRepository<StockBatch, Long> {

    // Finds every batch expiring on or before a given date - this is 
    // exactly what will power our future "expiry alert" feature
    List<StockBatch> findByExpiryDateLessThanEqual(LocalDate date);
}

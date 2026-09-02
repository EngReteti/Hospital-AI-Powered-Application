package com.amason.hospitalinventory.repository;

import com.amason.hospitalinventory.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

// Simplest repository so far - no custom methods needed yet, 
// just the free save/find/delete methods JpaRepository gives us
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}

package com.amason.hospitalinventory.repository;

import com.amason.hospitalinventory.model.StockMovement;
import com.amason.hospitalinventory.model.MovementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    // Finds every movement for one specific product - this is what our 
    // "calculate current stock" logic will use in the Service layer
    List<StockMovement> findByProductId(Long productId);

    // Finds every movement stuck in PENDING status - this is what will 
    // power the Auditor's "movements waiting for my approval" screen
    List<StockMovement> findByStatus(MovementStatus status);
}

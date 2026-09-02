package com.amason.hospitalinventory.service;

import com.amason.hospitalinventory.model.MovementStatus;
import com.amason.hospitalinventory.model.MovementType;
import com.amason.hospitalinventory.model.StockMovement;
import com.amason.hospitalinventory.repository.StockMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// @Service tells Spring: "this class holds business logic, manage it for me"
// This is different from @Entity (a table) and @Repository-style interfaces 
// (data access) - this is where actual DECISIONS get made
@Service
public class StockService {

    // @Autowired tells Spring: "automatically give me a working 
    // StockMovementRepository - I don't need to build it myself"
    @Autowired
    private StockMovementRepository stockMovementRepository;

    /**
     * Calculates the current stock level for one product by replaying 
     * its entire movement history - this is the formula we designed 
     * on paper, now turned into real code.
     *
     * current_stock = 
     *     SUM(IN, DIRECT) 
     *   - SUM(DISPENSED/TRANSFER/DAMAGE/EXPIRED, DIRECT) 
     *   ± SUM(ADJUSTMENT, APPROVED)
     */
    public int calculateCurrentStock(Long productId) {
        // Fetch every single movement ever recorded for this product
        List<StockMovement> movements = stockMovementRepository.findByProductId(productId);

        int stock = 0;

        // Go through each movement one at a time and adjust the running total
        for (StockMovement movement : movements) {

            // Only DIRECT and APPROVED movements count toward real stock.
            // PENDING and REJECTED movements are ignored completely - 
            // this is what makes the approval workflow actually matter
            boolean countsTowardStock = 
                movement.getStatus() == MovementStatus.DIRECT ||
                movement.getStatus() == MovementStatus.APPROVED;

            if (!countsTowardStock) {
                continue; // skip this movement, check the next one
            }

            // Decide whether this movement type ADDS or SUBTRACTS stock
            switch (movement.getType()) {
                case IN:
                    stock += movement.getQuantity();
                    break;

                case DISPENSED:
                case TRANSFER:
                case DAMAGE:
                case EXPIRED:
                    stock -= movement.getQuantity();
                    break;

                case ADJUSTMENT:
                    // An adjustment can go either direction - for now we 
                    // treat its quantity as the exact correction amount.
                    // We'll refine this once we build the reconciliation 
                    // feature (Phase 11), where adjustments get a clear 
                    // "increase" or "decrease" direction
                    stock += movement.getQuantity();
                    break;
            }
        }

        return stock;
    }
}

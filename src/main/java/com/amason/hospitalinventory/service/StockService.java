package com.amason.hospitalinventory.service;


import com.amason.hospitalinventory.model.MovementStatus;
import com.amason.hospitalinventory.model.MovementType;
import com.amason.hospitalinventory.model.StockMovement;
import com.amason.hospitalinventory.repository.StockMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
// Added these two imports near the top, with the others
import com.amason.hospitalinventory.model.Product;
import com.amason.hospitalinventory.repository.ProductRepository;

// @Service tells Spring: "this class holds business logic, manage it for me"
// This is different from @Entity (a table) and @Repository-style interfaces 
// (data access) - this is where actual DECISIONS get made
@Service
public class StockService {

    // @Autowired tells Spring: "automatically give me a working 
    // StockMovementRepository - I don't need to build it myself"
    @Autowired
    private StockMovementRepository stockMovementRepository;
    @Autowired
    private ProductRepository productRepository;
    /**
     * Records a new stock movement - this is the single entry point 
     * every stock change should go through, so our safety rules are 
     * always enforced in one place, never bypassed.
     */
    public StockMovement recordMovement(StockMovement movement) {

        // Step 1: figure out if this product is a controlled substance,
        // so we know whether approval is required
        Product product = movement.getProduct();
        boolean isControlled = product.getIsControlledSubstance();

        // Step 2: decide the correct starting status using the rule 
        // we just wrote above
        MovementStatus status = determineInitialStatus(movement.getType(), isControlled);
        movement.setStatus(status);

        // Step 3: SAFETY CHECK - if this movement would immediately reduce 
        // stock (and isn't waiting for approval), make sure it won't push 
        // stock below zero. This is what actually prevents "selling" more 
        // than physically exists
        boolean isReduction = 
            movement.getType() == MovementType.DISPENSED ||
            movement.getType() == MovementType.TRANSFER ||
            movement.getType() == MovementType.DAMAGE ||
            movement.getType() == MovementType.EXPIRED;

        if (isReduction && status == MovementStatus.DIRECT) {
            int currentStock = calculateCurrentStock(product.getId());

            if (currentStock < movement.getQuantity()) {
                // We stop here and refuse to save - throwing an exception 
                // is how Java signals "this operation cannot continue"
                throw new IllegalStateException(
                    "Cannot record movement: only " + currentStock + 
                    " units available, but " + movement.getQuantity() + " requested."
                );
            }
        }

        // Step 4: everything checked out
        return stockMovementRepository.save(movement);
    }/**
     
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
/**
     * Decides what STATUS a new movement should start with, before it's saved.
     * This is the actual enforcement of our two rules:
     *   1. ADJUSTMENT always needs approval first
     *   2. ANY movement on a controlled substance needs approval first,
     *      even a normal IN or DISPENSED
     * Everything else is safe to apply immediately (DIRECT).
     */
    public MovementStatus determineInitialStatus(MovementType type, boolean isControlledSubstance) {
        
        boolean requiresApproval = 
            type == MovementType.ADJUSTMENT || isControlledSubstance;

        if (requiresApproval) {
            return MovementStatus.PENDING;
        }

        return MovementStatus.DIRECT;
    }
}

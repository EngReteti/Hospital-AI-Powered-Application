package com.amason.hospitalinventory.model;

// Restricts every stock movement to exactly one of these types.
// This matches our schema design: nothing else can ever be recorded.
public enum MovementType {
    IN,           // Stock received from a supplier
    DISPENSED,    // Stock given out (e.g. sold, used on a patient)
    TRANSFER,     // Stock moved between departments
    DAMAGE,       // Stock written off as damaged/broken
    EXPIRED,      // Stock written off because it passed its expiry date
    ADJUSTMENT    // A correction after a physical count mismatch - 
                  // this is the one that requires Auditor approval
}

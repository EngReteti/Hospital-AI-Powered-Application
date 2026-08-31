package com.amason.hospitalinventory.model;

// Tracks whether a movement counts toward stock yet.
public enum MovementStatus {
    DIRECT,    // Applied immediately - used for IN, DISPENSED, TRANSFER, DAMAGE, EXPIRED
    PENDING,   // Waiting for Auditor approval - used only for ADJUSTMENT
    APPROVED,  // Auditor approved it - now counts toward stock
    REJECTED   // Auditor rejected it - never counts toward stock
}

package com.amason.hospitalinventory.model;

// An enum restricts a field to only these exact values - nothing else
// can ever be assigned to "role". This matches our schema decision:
// ADMIN, STOREKEEPER, AUDITOR only
public enum Role {
    ADMIN,
    STOREKEEPER,
    AUDITOR
}

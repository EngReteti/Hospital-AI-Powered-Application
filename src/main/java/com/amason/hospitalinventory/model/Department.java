package com.amason.hospitalinventory.model;

import jakarta.persistence.*;

// Turns this class into the "departments" table - matches our schema design
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Required - e.g. "ICU", "Pharmacy", "Surgery", "General Ward"
    @Column(nullable = false)
    private String name;

    // A short code used in reports (e.g. "ICU", "PHM", "SUR") - 
    // required and must be unique so two departments can't share a code
    @Column(nullable = false, unique = true)
    private String locationCode;

    // --- Getters and setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }
}

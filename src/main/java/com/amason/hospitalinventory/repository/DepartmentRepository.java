package com.amason.hospitalinventory.repository;

import com.amason.hospitalinventory.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}

package com.amason.hospitalinventory.repository;

import com.amason.hospitalinventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Spring reads this method name and generates:
    // "SELECT * FROM products WHERE reorder_level >= (calculated stock)"
    // We'll actually use this later once stock calculation logic exists -
    // for now it's just declared, ready to be used by the Service layer
    List<Product> findByCategory(String category);
}

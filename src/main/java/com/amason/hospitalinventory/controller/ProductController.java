package com.amason.hospitalinventory.controller;

import com.amason.hospitalinventory.model.Product;
import com.amason.hospitalinventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// @RestController tells Spring: "this class handles web requests and 
// sends back data (usually as JSON), not HTML pages"
// @RequestMapping sets the base web address for everything in this class - 
// so every endpoint below starts with "/api/products"
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // @GetMapping means: "when someone visits this address using GET 
    // (just asking for data), run this method." 
    // Full address: GET /api/products
    // This returns EVERY product - Spring automatically converts our 
    // List<Product> into JSON for us, no extra code needed
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // {id} in the path means "this part of the URL is a variable."
    // @PathVariable grabs that value and puts it into our "id" parameter.
    // Full address example: GET /api/products/5
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // @PostMapping means: "when someone SENDS data to this address, run this."
    // @RequestBody takes the JSON someone sends us and automatically 
    // converts it into a real Product object
    // Full address: POST /api/products
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }
}

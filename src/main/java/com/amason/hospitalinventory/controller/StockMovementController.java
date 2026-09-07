package com.amason.hospitalinventory.controller;

import com.amason.hospitalinventory.model.StockMovement;
import com.amason.hospitalinventory.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    // Notice we're using StockService here, NOT the repository directly -
    // this is important. The Controller's job is just to receive the 
    // request and pass it along; the Service is what applies our safety 
    // rules (approval logic, negative-stock blocking). If we called the 
    // repository directly here, we'd accidentally skip all that protection
    @Autowired
    private StockService stockService;

    @PostMapping
    public StockMovement recordMovement(@RequestBody StockMovement movement) {
        return stockService.recordMovement(movement);
    }
}

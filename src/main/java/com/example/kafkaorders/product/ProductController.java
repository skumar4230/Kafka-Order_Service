package com.example.kafkaorders.product;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductCacheService cacheService;

    public ProductController(ProductCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/{id}")
    public String get(@PathVariable String id) {
        return cacheService.getProduct(id);
    }
}

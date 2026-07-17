package com.example.galileo.feature.product.dto;

import java.math.BigDecimal;

import com.example.galileo.feature.product.Product;

public record ProductResponse(Long id, String name, String description, BigDecimal price) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}

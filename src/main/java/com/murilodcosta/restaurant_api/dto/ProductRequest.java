package com.murilodcosta.restaurant_api.dto;

import com.murilodcosta.restaurant_api.domain.entity.Product;
import com.murilodcosta.restaurant_api.domain.entity.ProductCategory;

import java.math.BigDecimal;

public record ProductRequest(
        String name,
        Long categoryId,
        String description,
        BigDecimal price,
        Boolean available,
        Integer preparationTime
) {
    public Product toEntity(ProductCategory category) {
        Product product = new Product();
        preencher(product, category);
        return product;
    }

    public void preencher(Product product, ProductCategory category) {
        product.setName(name);
        product.setCategory(category);
        product.setDescription(description);
        product.setPrice(price);
        product.setAvailable(available != null ? available : true);
        product.setPreparationTime(preparationTime);
    }
}

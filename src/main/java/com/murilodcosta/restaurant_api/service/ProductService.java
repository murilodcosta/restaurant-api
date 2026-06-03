package com.murilodcosta.restaurant_api.service;

import com.murilodcosta.restaurant_api.domain.entity.Product;
import com.murilodcosta.restaurant_api.domain.entity.ProductCategory;
import com.murilodcosta.restaurant_api.dto.ProductRequest;
import com.murilodcosta.restaurant_api.dto.ProductResponse;
import com.murilodcosta.restaurant_api.repository.ProductCategoryRepository;
import com.murilodcosta.restaurant_api.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private ProductCategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse create(ProductRequest productRequest){
        ProductCategory productCategory = findCategoryById(productRequest.categoryId());
        var product = productRequest.toEntity(productCategory);
        var savedProduct = productRepository.save(product);
        return ProductResponse.fromEntity(savedProduct);
    }

    public Page<ProductResponse> listAll(Pageable pageable){
        return productRepository.findAll(pageable).map(ProductResponse::fromEntity);
    }

    public ProductResponse findById(Long id){
        var product = findProductById(id);
        return ProductResponse.fromEntity(product);
    }

    public ProductResponse update(Long id, ProductRequest productRequest){
        var product = findProductById(id);
        ProductCategory category = findCategoryById(productRequest.categoryId());
        productRequest.preencher(product, category);
        var updatedProduct = productRepository.save(product);
        return ProductResponse.fromEntity(updatedProduct);
    }

    public void delete(Long id){
        var product = findProductById(id);
        productRepository.delete(product);
    }

    private Product findProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private ProductCategory findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
}

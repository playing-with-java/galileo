package com.example.galileo.feature.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.galileo.exception.ResourceNotFoundException;
import com.example.galileo.feature.product.dto.ProductRequest;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Page<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    @Transactional
    public Product create(ProductRequest request) {
        Product product = new Product(request.name(), request.description(), request.price());
        return repository.save(product);
    }

    @Transactional
    public Product update(Long id, ProductRequest request) {
        Product product = findById(id);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        return repository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = findById(id);
        repository.delete(product);
    }
}

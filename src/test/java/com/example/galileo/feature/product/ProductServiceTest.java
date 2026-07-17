package com.example.galileo.feature.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.galileo.exception.ResourceNotFoundException;
import com.example.galileo.feature.product.dto.ProductRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldReturnPageWhenFindAll() {
        Product product = new Product("Product A", "Description", BigDecimal.TEN);
        Page<Product> page = new PageImpl<>(List.of(product));
        Pageable pageable = PageRequest.of(0, 20);

        when(repository.findAll(pageable)).thenReturn(page);

        Page<Product> result = productService.findAll(pageable);

        assertThat(result.getContent()).containsExactly(product);
    }

    @Test
    void shouldReturnProductWhenFoundById() {
        Product product = new Product("Product A", "Description", BigDecimal.TEN);
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findById(1L);

        assertThat(result).isEqualTo(product);
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found with id 1");
    }

    @Test
    void shouldCreateProduct() {
        ProductRequest request = new ProductRequest("Product A", "Description", BigDecimal.TEN);
        Product product = new Product("Product A", "Description", BigDecimal.TEN);

        when(repository.save(any(Product.class))).thenReturn(product);

        Product result = productService.create(request);

        assertThat(result.getName()).isEqualTo("Product A");
        verify(repository).save(any(Product.class));
    }

    @Test
    void shouldUpdateProduct() {
        Product existing = new Product("Product A", "Description", BigDecimal.TEN);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        ProductRequest request = new ProductRequest("Product B", "Updated", BigDecimal.valueOf(15));
        when(repository.save(existing)).thenReturn(existing);

        Product result = productService.update(1L, request);

        assertThat(result.getName()).isEqualTo("Product B");
        assertThat(result.getDescription()).isEqualTo("Updated");
        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(15));
        verify(repository).save(existing);
    }

    @Test
    void shouldDeleteProduct() {
        Product existing = new Product("Product A", "Description", BigDecimal.TEN);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        productService.delete(1L);

        verify(repository).delete(existing);
    }
}

package com.example.galileo.feature.product;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.galileo.feature.product.dto.ProductRequest;
import com.example.galileo.feature.product.dto.ProductResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    void shouldReturnProductPageWhenGetAll() {
        Product product = new Product("Product A", "Description", BigDecimal.TEN);
        product.setId(1L);
        Page<Product> page = new PageImpl<>(List.of(product));
        Pageable pageable = PageRequest.of(0, 10);

        when(productService.findAll(pageable)).thenReturn(page);

        Page<ProductResponse> response = productController.getAll(pageable);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).id()).isEqualTo(1L);
        verify(productService).findAll(pageable);
    }

    @Test
    void shouldReturnProductResponseWhenGetById() {
        Product product = new Product("Product A", "Description", BigDecimal.TEN);
        product.setId(1L);

        when(productService.findById(1L)).thenReturn(product);

        ProductResponse response = productController.getById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Product A");
        verify(productService).findById(1L);
    }

    @Test
    void shouldCreateProductAndReturnCreatedResponse() {
        ProductRequest request = new ProductRequest("Product A", "Description", BigDecimal.TEN);
        Product product = new Product("Product A", "Description", BigDecimal.TEN);
        product.setId(1L);

        when(productService.create(request)).thenReturn(product);

        ResponseEntity<ProductResponse> response = productController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(1L);
        verify(productService).create(request);
    }

    @Test
    void shouldUpdateProductAndReturnUpdatedValue() {
        ProductRequest request = new ProductRequest("Product B", "Updated", BigDecimal.valueOf(20));
        Product product = new Product("Product B", "Updated", BigDecimal.valueOf(20));
        product.setId(2L);

        when(productService.update(2L, request)).thenReturn(product);

        ProductResponse response = productController.update(2L, request);

        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.name()).isEqualTo("Product B");
        verify(productService).update(2L, request);
    }

    @Test
    void shouldDeleteProductAndReturnNoContent() {
        ResponseEntity<Void> response = productController.delete(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(productService).delete(1L);
    }
}

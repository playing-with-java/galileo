package com.example.galileo.feature.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.galileo.feature.product.dto.ProductRequest;
import com.example.galileo.feature.product.dto.ProductResponse;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "APIs para gestionar productos")
public class ProductController {

    private final ProductService service;

    @Operation(summary = "Listar productos", description = "Obtiene el listado completo de productos disponibles")
    @GetMapping
    public Page<ProductResponse> getAll(@ParameterObject Pageable pageable) {
        return service.findAll(pageable).map(ProductResponse::from);
    }

    @Operation(summary = "Obtener producto", description = "Busca un producto por su id")
    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return ProductResponse.from(service.findById(id));
    }

    @Operation(summary = "Crear producto", description = "Crea un nuevo producto en la base de datos")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        Product created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.from(created));
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente")
    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return ProductResponse.from(service.update(id, request));
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto por su id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

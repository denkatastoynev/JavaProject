package com.uni.projectmanager.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "Endpoints for managing products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Create a new product", description = "Add a new product to the database")
        @PostMapping
        public ResponseEntity<?> createProduct(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Product object to be created",
            content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                name = "Product Example",
                value = "{ \"name\": \"Sample Product\", \"price\": 100 }"
            )
            )
        )
        @RequestBody Product product) {
        if (product == null || product.getName() == null || product.getPrice() == null) {
            return ResponseEntity.badRequest().body("Product name and price must not be null");
        }
        try {
            // Ensure the user is in a managed state
            if (product.getId() != null && productRepository.existsById(product.getId())) {
                product = productRepository.findById(product.getId()).get();
            }
            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving product: " + e.getMessage());
        }
    }

    @Operation(summary = "Update a product", description = "Update an existing product's details")
    @PutMapping("/{id}")
    public Product updateUser(
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Product object with updated details",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Updated Product Example",
                    value = "{ \"name\": \"Updated Product\", \"price\": 100 }"
                )
            )
        )
        @RequestBody Product product) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Product not found for update with id: " + id));
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        return productRepository.save(existingProduct);
    }

    @Operation(summary = "Delete a product", description = "Delete a product by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.deleteById(id);
                    return ResponseEntity.ok("Product with id: " + id + " deleted successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

package com.example.javatest.product;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllProducts() throws Exception {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product A");
        product1.setPrice(10.0);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product B");
        product2.setPrice(20.0);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/api/products"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value("Product A"))
               .andExpect(jsonPath("$[1].name").value("Product B"));
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product A");
        product.setPrice(10.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Product A"));
    }

    @Test
    void testCreateProduct() throws Exception {
        Product product = new Product();
        product.setName("Product A");
        product.setPrice(10.0);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Product A"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Product A");
        existingProduct.setPrice(10.0);

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product A");
        updatedProduct.setPrice(15.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Updated Product A"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product A");
        product.setPrice(10.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(1L);

        mockMvc.perform(delete("/api/products/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("Product with id: 1 deleted successfully"));
    }
}

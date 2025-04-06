package com.uni.projectmanager.dao;

import com.uni.projectmanager.product.Product;
import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    void save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void deleteById(Long id);
}
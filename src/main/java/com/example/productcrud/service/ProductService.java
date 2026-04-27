package com.example.productcrud.service;

import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // =========================
    // GET ALL + PAGINATION
    // =========================
    public Page<Product> findAllByOwner(User owner, Pageable pageable) {
        return productRepository.findByOwner(owner, pageable);
    }

    // =========================
    // SEARCH (PAGINATION)
    // =========================
    public Page<Product> searchProducts(User owner, String keyword, Pageable pageable) {
        return productRepository.findByOwnerAndNameContainingIgnoreCase(owner, keyword, pageable);
    }

    // =========================
    // SEARCH + FILTER CATEGORY (FIXED)
    // =========================
    public Page<Product> searchProducts(User owner, String keyword, Long categoryId, Pageable pageable) {

        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        return productRepository.searchAndFilterByOwner(owner, keyword, categoryId, pageable);
    }

    // =========================
    // GET BY ID + OWNER
    // =========================
    public Optional<Product> findByIdAndOwner(Long id, User owner) {
        return productRepository.findByIdAndOwner(id, owner);
    }

    // =========================
    // SAVE
    // =========================
    public Product save(Product product) {
        System.out.println(product);
         return productRepository.save(product);
    }

    // =========================
    // DELETE
    // =========================
    public void deleteByIdAndOwner(Long id, User owner) {
        productRepository.findByIdAndOwner(id, owner)
                .ifPresent(productRepository::delete);
    }
}
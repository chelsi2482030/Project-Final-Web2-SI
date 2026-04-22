package com.example.productcrud.service;

/*chelsi*/

import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAllByOwner(User owner) {
        return productRepository.findByOwner(owner);
    }


    public Optional<Product> findByIdAndOwner(Long id, User owner) {
        return productRepository.findByIdAndOwner(id, owner);
    }


    public Product save(Product product) {
        return productRepository.save(product);
    }


    public void deleteByIdAndOwner(Long id, User owner) {
        productRepository.findByIdAndOwner(id, owner)
                .ifPresent(productRepository::delete);
    }


    public List<Product> searchProducts(User owner, String keyword, Long categoryId) {

        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        return productRepository.searchAndFilterByOwner(owner, keyword, categoryId);
    }
}
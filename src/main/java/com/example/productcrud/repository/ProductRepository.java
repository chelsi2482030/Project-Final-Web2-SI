package com.example.productcrud.repository;

import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // =========================
    // GET ALL + PAGINATION
    // =========================
    Page<Product> findByOwner(User owner, Pageable pageable);

    // =========================
    // SEARCH + PAGINATION
    // =========================
    Page<Product> findByOwnerAndNameContainingIgnoreCase(
            User owner,
            String keyword,
            Pageable pageable
    );

    // =========================
    // SEARCH + FILTER (ADVANCED)
    // =========================
    @Query("""
        SELECT p FROM Product p
        WHERE p.owner = :owner
        AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:categoryId IS NULL OR p.category.id = :categoryId)
    """)
    Page<Product> searchAndFilterByOwner(
            @Param("owner") User owner,
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    // =========================
    // GET BY ID + OWNER
    // =========================
    Optional<Product> findByIdAndOwner(Long id, User owner);
}
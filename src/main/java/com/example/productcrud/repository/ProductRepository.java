package com.example.productcrud.repository;

import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT p FROM Product p
        WHERE p.owner = :owner
        AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:categoryId IS NULL OR p.category.id = :categoryId)
    """)
    List<Product> searchAndFilterByOwner(
            @Param("owner") User owner,
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId
    );

    List<Product> findByOwner(User owner);

    Optional<Product> findByIdAndOwner(Long id, User owner);
}
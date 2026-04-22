package com.example.productcrud.repository;

import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
=======
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
>>>>>>> 9caa1780412651a943f017901a7572bfb276c944

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

<<<<<<< HEAD
    Page<Product> findByOwner(User owner, Pageable pageable);

    Page<Product> findByOwnerAndNameContainingIgnoreCase(User owner, String keyword, Pageable pageable);
=======
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
>>>>>>> 9caa1780412651a943f017901a7572bfb276c944

    Optional<Product> findByIdAndOwner(Long id, User owner);
}
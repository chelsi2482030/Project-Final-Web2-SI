package com.example.productcrud.repository;

import com.example.productcrud.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Categories, Long> {
}
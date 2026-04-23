package com.example.productcrud.service;

import com.example.productcrud.model.Category;
import com.example.productcrud.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category tidak ditemukan"));
    }

    public void save(Category category) {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new RuntimeException("Nama kategori wajib diisi");
        }
        categoryRepository.save(category);
    }

    public void delete(Long id) {
        Category category = findById(id);

        // 🔥 CEGAH DELETE kalau masih dipakai product
        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            throw new RuntimeException("Kategori masih digunakan oleh produk!");
        }

        categoryRepository.delete(category);
    }
}
package com.example.productcrud.service;

import com.example.productcrud.model.Categories;
import com.example.productcrud.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Categories> findAll() {
        return categoryRepository.findAll();
    }

    public Categories findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category tidak ditemukan"));
    }

    public void save(Categories categories) {
        if (categories.getName() == null || categories.getName().isBlank()) {
            throw new RuntimeException("Nama kategori wajib diisi");
        }
        categoryRepository.save(categories);
    }

    public void delete(Long id) {
        Categories categories = findById(id);

        // 🔥 CEGAH DELETE kalau masih dipakai product
        if (categories.getProducts() != null && !categories.getProducts().isEmpty()) {
            throw new RuntimeException("Kategori masih digunakan oleh produk!");
        }

        categoryRepository.delete(categories);
    }
}
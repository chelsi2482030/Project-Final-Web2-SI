package com.example.productcrud;

import com.example.productcrud.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCrudApplication.class, args);
    }


    @Bean
    public CommandLineRunner debugCategories(CategoryRepository categoryRepo) {
        return args -> {
            System.out.println("=== DAFTAR KATEGORI DI DATABASE ===");
            categoryRepo.findAll().forEach(cat -> {
                System.out.println("ID: " + cat.getId() + " | Nama: " + cat.getName());
            });
            System.out.println("====================================");
        };
    }
}



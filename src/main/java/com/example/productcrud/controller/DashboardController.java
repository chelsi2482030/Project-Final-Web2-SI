package com.example.productcrud.controller;

import com.example.productcrud.repository.ProductRepository;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.repository.CategoryRepository; // ✅ TAMBAH

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        long totalProduct = productRepository.count();
        long totalUser = userRepository.count();
        long totalCategory = categoryRepository.count();

        model.addAttribute("totalProduct", totalProduct);
        model.addAttribute("totalUser", totalUser);
        model.addAttribute("totalCategory", totalCategory);

        return "dashboard";
    }
}
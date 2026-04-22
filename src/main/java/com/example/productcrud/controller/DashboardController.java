package com.example.productcrud.controller;

/* Bela */

import com.example.productcrud.repository.ProductRepository;
import com.example.productcrud.repository.UserRepository;
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

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        long totalProduct = productRepository.count();
        long totalUser = userRepository.count();

        model.addAttribute("totalProduct", totalProduct);
        model.addAttribute("totalUser", totalUser);

        return "dashboard";
    }
}
package com.example.productcrud.controller;

import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final ProductService productService;
    private final UserRepository userRepository;

    public DashboardController(ProductService productService, UserRepository userRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(UserDetails userDetails) {
        if (userDetails == null) throw new RuntimeException("User belum login");
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";

        User currentUser = getCurrentUser(userDetails);

        // Mengambil semua produk milik user
        Page<Product> page = productService.findAllByOwner(currentUser, Pageable.unpaged());
        List<Product> products = page.getContent();

        // Mengirim data ke template HTML
        model.addAttribute("user", currentUser);
        model.addAttribute("totalProduk", page.getTotalElements());
        model.addAttribute("inventoryValue", products.stream().mapToDouble(p -> p.getPrice() * p.getStock()).sum());
        model.addAttribute("aktif", products.stream().filter(Product::isActive).count());
        model.addAttribute("nonAktif", page.getTotalElements() - products.stream().filter(Product::isActive).count());
        model.addAttribute("lowStockProducts", products.stream().filter(p -> p.getStock() < 5).toList());

        return "dashboard";
    }
}
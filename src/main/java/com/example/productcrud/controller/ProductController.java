package com.example.productcrud.controller;

import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;
import com.example.productcrud.model.Category;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.repository.CategoryRepository;
import com.example.productcrud.service.ProductService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductService productService,
                             UserRepository userRepository,
                             CategoryRepository categoryRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    private User getCurrentUser(UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("User belum login");
        }

        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    // =========================
    // DASHBOARD (HOME)
    // =========================
    @GetMapping("/")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        User currentUser = getCurrentUser(userDetails);

        Page<Product> page = productService.findAllByOwner(currentUser, Pageable.unpaged());
        List<Product> products = page.getContent();

        long totalProduk = page.getTotalElements();

        double inventoryValue = products.stream()
                .mapToDouble(p -> p.getPrice() * p.getStock())
                .sum();

        long aktif = products.stream()
                .filter(Product::isActive)
                .count();

        long nonAktif = totalProduk - aktif;

        List<Product> lowStock = products.stream()
                .filter(p -> p.getStock() < 5)
                .toList();

        model.addAttribute("totalProduk", totalProduk);
        model.addAttribute("inventoryValue", inventoryValue);
        model.addAttribute("aktif", aktif);
        model.addAttribute("nonAktif", nonAktif);
        model.addAttribute("lowStockProducts", lowStock);

        return "dashboard";
    }

    // =========================
    // LIST PRODUCT
    // =========================
    @GetMapping("/products")
    public String listProducts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long category,
            @PageableDefault(size = 5) Pageable pageable,
            Model model) {

        User currentUser = getCurrentUser(userDetails);

        Page<Product> products;

        if ((keyword != null && !keyword.isBlank()) || category != null) {
            products = productService.searchProducts(currentUser, keyword, category, pageable);
        } else {
            products = productService.findAllByOwner(currentUser, pageable);
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);

        return "product/list";
    }

    // =========================
    // CREATE
    // =========================
    @GetMapping("/products/new")
    public String showCreateForm(Model model) {
        Product product = new Product();
        product.setCreatedAt(LocalDate.now());

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());

        return "product/form";
    }

    // =========================
    // SAVE (FIX CATEGORY)
    // =========================
    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam("category.id") Long categoryId,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {

        User currentUser = getCurrentUser(userDetails);

        Category category = categoryRepository.findById(categoryId).orElse(null);
        product.setCategory(category);

        product.setOwner(currentUser);
        productService.save(product);

        redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil disimpan!");
        return "redirect:/products";
    }

    // =========================
    // DELETE
    // =========================
    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {

        User currentUser = getCurrentUser(userDetails);

        if (productService.findByIdAndOwner(id, currentUser).isPresent()) {
            productService.deleteByIdAndOwner(id, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil dihapus!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Produk tidak ditemukan.");
        }

        return "redirect:/products";
    }
}
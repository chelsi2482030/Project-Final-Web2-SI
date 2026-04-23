package com.example.productcrud.controller;

import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.repository.CategoryRepository;
import com.example.productcrud.service.ProductService;

import java.time.LocalDate;

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
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/products";
    }

    // =========================
    // LIST PRODUCT + SEARCH + FILTER + PAGINATION (FIXED)
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

        // kondisi: ada search/filter atau tidak
        if ((keyword != null && !keyword.isBlank()) || category != null) {
            products = productService.searchProducts(currentUser, keyword, category, pageable);
        } else {
            products = productService.findAllByOwner(currentUser, pageable);
        }

        var categories = categoryRepository.findAll();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);

        return "product/list";
    }

    // =========================
    // DETAIL
    // =========================
    @GetMapping("/products/{id}")
    public String detailProduct(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        User currentUser = getCurrentUser(userDetails);

        return productService.findByIdAndOwner(id, currentUser)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product/detail";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Produk tidak ditemukan.");
                    return "redirect:/products";
                });
    }

    // =========================
    // CREATE FORM
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
    // SAVE
    // =========================
    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {

        User currentUser = getCurrentUser(userDetails);

        if (product.getId() != null) {
            boolean isOwner = productService
                    .findByIdAndOwner(product.getId(), currentUser)
                    .isPresent();

            if (!isOwner) {
                redirectAttributes.addFlashAttribute("errorMessage", "Produk tidak ditemukan.");
                return "redirect:/products";
            }
        }

        product.setOwner(currentUser);
        productService.save(product);

        redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil disimpan!");
        return "redirect:/products";
    }

    // =========================
    // EDIT
    // =========================
    @GetMapping("/products/{id}/edit")
    public String showEditForm(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        User currentUser = getCurrentUser(userDetails);

        return productService.findByIdAndOwner(id, currentUser)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("categories", categoryRepository.findAll());
                    return "product/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Produk tidak ditemukan.");
                    return "redirect:/products";
                });
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
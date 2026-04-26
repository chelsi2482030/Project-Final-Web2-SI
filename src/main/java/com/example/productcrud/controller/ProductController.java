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

    @GetMapping("/")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";

        User currentUser = getCurrentUser(userDetails);
<<<<<<< HEAD
        model.addAttribute("user", currentUser);
=======

>>>>>>> 63a2e0104797e190e176133b9438b0efa19c10af
        Page<Product> page = productService.findAllByOwner(currentUser, Pageable.unpaged());
        List<Product> products = page.getContent();
        model.addAttribute("totalProduk", page.getTotalElements());
<<<<<<< HEAD
        model.addAttribute("inventoryValue", products.stream().mapToDouble(p -> p.getPrice() * p.getStock()).sum());
        model.addAttribute("aktif", products.stream().filter(Product::isActive).count());
        model.addAttribute("nonAktif", page.getTotalElements() - products.stream().filter(Product::isActive).count());
        model.addAttribute("lowStockProducts", products.stream().filter(p -> p.getStock() < 5).toList());
=======
        model.addAttribute("inventoryValue",
                products.stream().mapToDouble(p -> p.getPrice() * p.getStock()).sum());
        model.addAttribute("aktif",
                products.stream().filter(Product::isActive).count());
        model.addAttribute("nonAktif",
                page.getTotalElements() - products.stream().filter(Product::isActive).count());
        model.addAttribute("lowStockProducts",
                products.stream().filter(p -> p.getStock() < 5).toList());

>>>>>>> 63a2e0104797e190e176133b9438b0efa19c10af
        return "dashboard";
    }

    @GetMapping("/products")
<<<<<<< HEAD
    public String listProducts(@AuthenticationPrincipal UserDetails userDetails,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Long category,
                               @PageableDefault(size = 10) Pageable pageable,
                               Model model) {
        if (userDetails == null) return "redirect:/login";
=======
    public String listProducts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long category,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {

>>>>>>> 63a2e0104797e190e176133b9438b0efa19c10af
        User currentUser = getCurrentUser(userDetails);
        Page<Product> products;
        if ((keyword != null && !keyword.isBlank()) || category != null) {
            products = productService.searchProducts(currentUser, keyword, category, pageable);
        } else {
            products = productService.findAllByOwner(currentUser, pageable);
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        return "product/list";
    }

    @GetMapping("/products/new")
    public String showCreateForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";
        User currentUser = getCurrentUser(userDetails);
        Product product = new Product();
        product.setCreatedAt(LocalDate.now());
<<<<<<< HEAD
        model.addAttribute("user", currentUser);
=======

>>>>>>> 63a2e0104797e190e176133b9438b0efa19c10af
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());

        return "product/form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product,
<<<<<<< HEAD
                              @RequestParam("categoryId") Long categoryId,
=======
>>>>>>> 63a2e0104797e190e176133b9438b0efa19c10af
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        if (userDetails == null) return "redirect:/login";
        User currentUser = getCurrentUser(userDetails);
<<<<<<< HEAD
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category tidak ditemukan"));
        product.setCategory(category);
=======

        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository
                    .findById(product.getCategory().getId())
                    .orElse(null);

            product.setCategory(category);
        }

>>>>>>> 63a2e0104797e190e176133b9438b0efa19c10af
        product.setOwner(currentUser);
        productService.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil disimpan!");
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
<<<<<<< HEAD
        if (userDetails == null) return "redirect:/login";
=======

>>>>>>> 63a2e0104797e190e176133b9438b0efa19c10af
        User currentUser = getCurrentUser(userDetails);

        if (productService.findByIdAndOwner(id, currentUser).isPresent()) {
            productService.deleteByIdAndOwner(id, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil dihapus!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Produk tidak ditemukan.");
        }

        return "redirect:/products";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";
        User currentUser = getCurrentUser(userDetails);
        model.addAttribute("user", currentUser);
        return "profile";
    }

    @GetMapping("/edit-profile")
    public String editProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";
        User currentUser = getCurrentUser(userDetails);
        model.addAttribute("user", currentUser);
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam(required = false) String fullName,
                                @RequestParam(required = false) String phoneNumber,
                                @RequestParam(required = false) String address,
                                @RequestParam(required = false) String bio,
                                RedirectAttributes redirectAttributes) {
        if (userDetails == null) return "redirect:/login";
        User currentUser = getCurrentUser(userDetails);

        currentUser.setFullName(fullName);
        currentUser.setPhoneNumber(phoneNumber);
        currentUser.setAddress(address);
        currentUser.setBio(bio);

        userRepository.save(currentUser);

        redirectAttributes.addFlashAttribute("successMessage", "Profile berhasil diupdate!");
        return "redirect:/profile";
    }
}
package com.example.productcrud.controller;

import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;
import com.example.productcrud.model.Category;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.repository.CategoryRepository;
import com.example.productcrud.service.ProductService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
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

    // TAMBAHKAN INI: Untuk mencegah error 400 jika ada input string kosong (seperti tanggal atau ID)
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
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
        model.addAttribute("user", currentUser);

        Page<Product> page = productService.findAllByOwner(currentUser, Pageable.unpaged());
        List<Product> products = page.getContent();

        model.addAttribute("totalProduk", page.getTotalElements());
        model.addAttribute("inventoryValue", products.stream().mapToDouble(p -> p.getPrice() * p.getStock()).sum());
        model.addAttribute("aktif", products.stream().filter(Product::isActive).count());
        model.addAttribute("nonAktif", page.getTotalElements() - products.stream().filter(Product::isActive).count());
        model.addAttribute("lowStockProducts", products.stream().filter(p -> p.getStock() < 5).toList());

        return "dashboard";
    }

    @GetMapping("/products")
    public String listProducts(@AuthenticationPrincipal UserDetails userDetails,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Long category,
                               @PageableDefault(size = 10) Pageable pageable,
                               Model model) {
        if (userDetails == null) return "redirect:/login";
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
        product.setActive(true); // Default aktif saat tambah baru

        model.addAttribute("user", currentUser);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/form";
    }

    // TAMBAHKAN Mapping Edit (Penting untuk fungsionalitas CRUD)
    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        User currentUser = getCurrentUser(userDetails);
        Product product = productService.findByIdAndOwner(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        model.addAttribute("user", currentUser);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam(value = "categoryId", required = false) Long categoryId,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        if (userDetails == null) return "redirect:/login";
        User currentUser = getCurrentUser(userDetails);

        // Validasi Kategori agar tidak error 400 jika user lupa pilih
        if (categoryId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Silakan pilih kategori!");
            return "redirect:/products/new";
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category tidak ditemukan"));

        product.setCategory(category);
        product.setOwner(currentUser);

        // Pastikan tanggal dibuat tidak hilang saat update atau baru
        if (product.getId() == null && product.getCreatedAt() == null) {
            product.setCreatedAt(LocalDate.now());
        }

        productService.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil disimpan!");
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(userDetails);
        if (productService.findByIdAndOwner(id, currentUser).isPresent()) {
            productService.deleteByIdAndOwner(id, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil dihapus!");
        }
        return "redirect:/products";
    }

    // Metode profile tetap sama...
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";
        model.addAttribute("user", getCurrentUser(userDetails));
        return "profile";
    }

    @GetMapping("/edit-profile")
    public String editProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";
        model.addAttribute("user", getCurrentUser(userDetails));
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam(required = false) String fullName,
                                @RequestParam(required = false) String phoneNumber,
                                @RequestParam(required = false) String address,
                                @RequestParam(required = false) String bio,
                                RedirectAttributes redirectAttributes) {
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
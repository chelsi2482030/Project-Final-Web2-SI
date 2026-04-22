package com.example.productcrud.controller;


/* chelsi */
>>>>>>> 8ba328e (fix merge)
=======
>>>>>>> 3f3a164bbfbeea432ca1fff3d3b20c60e52e0659
import com.example.productcrud.model.Product;
import com.example.productcrud.model.User;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.service.ProductService;
<<<<<<< HEAD
import java.time.LocalDate;
=======

import java.util.Locale;

>>>>>>> 3f3a164bbfbeea432ca1fff3d3b20c60e52e0659
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

    // 🔍 LIST PRODUCT + SEARCH + FILTER
    @GetMapping("/products")
    public String listProducts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long category,
            Model model) {

        User currentUser = getCurrentUser(userDetails);

        var products = productService.searchProducts(currentUser, keyword, category);
        var categories = categoryRepository.findAll();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);

        return "product/list";
    }

<<<<<<< HEAD
    @GetMapping("/products/{id}")
    public String detailProduct(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model, RedirectAttributes redirectAttributes) {
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

=======
>>>>>>> 3f3a164bbfbeea432ca1fff3d3b20c60e52e0659
    @GetMapping("/products/new")
    public String showCreateForm(Model model) {
        Product product = new Product();
        product.setCreatedAt(LocalDate.now());
<<<<<<< HEAD
        model.addAttribute("product", product);
        model.addAttribute("categories", Category.values());
=======

        model.addAttribute("product", product);
        model.addAttribute("categories", Locale.Category.values());

>>>>>>> 3f3a164bbfbeea432ca1fff3d3b20c60e52e0659
        return "product/form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
<<<<<<< HEAD
        User currentUser = getCurrentUser(userDetails);

        if (product.getId() != null) {
            boolean isOwner = productService.findByIdAndOwner(product.getId(), currentUser).isPresent();
            if (!isOwner) {
                redirectAttributes.addFlashAttribute("errorMessage", "Produk tidak ditemukan.");
                return "redirect:/products";
            }
        }

        product.setOwner(currentUser);
        productService.save(product);
=======

        User currentUser = getCurrentUser(userDetails);

        product.setOwner(currentUser);
        productService.save(product);

>>>>>>> 3f3a164bbfbeea432ca1fff3d3b20c60e52e0659
        redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil disimpan!");
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/edit")
    public String showEditForm(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model, RedirectAttributes redirectAttributes) {
<<<<<<< HEAD
        User currentUser = getCurrentUser(userDetails);
        return productService.findByIdAndOwner(id, currentUser)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("categories", Category.values());
                    return "product/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Produk tidak ditemukan.");
=======

        User currentUser = getCurrentUser(userDetails);

        return productService.findByIdAndOwner(id, currentUser)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("categories", Locale.Category.values());
                    return "product/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Produk tidak ditemukan.");
>>>>>>> 3f3a164bbfbeea432ca1fff3d3b20c60e52e0659
                    return "redirect:/products";
                });
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
<<<<<<< HEAD
        User currentUser = getCurrentUser(userDetails);
        boolean isOwner = productService.findByIdAndOwner(id, currentUser).isPresent();

        if (isOwner) {
=======

        User currentUser = getCurrentUser(userDetails);

        if (productService.findByIdAndOwner(id, currentUser).isPresent()) {
>>>>>>> 3f3a164bbfbeea432ca1fff3d3b20c60e52e0659
            productService.deleteByIdAndOwner(id, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil dihapus!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Produk tidak ditemukan.");
        }

        return "redirect:/products";
    }
<<<<<<< HEAD
<<<<<<< HEAD
}
=======
}
>>>>>>> 3f3a164bbfbeea432ca1fff3d3b20c60e52e0659
=======
}
>>>>>>> 39856b1 (chelsi menambahkan)

package com.example.productcrud.controller;

import com.example.productcrud.model.Category;
import com.example.productcrud.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "category/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("category") Category category,
                       Model model) {
        try {
            categoryService.save(category);
            return "redirect:/categories";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "category/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        try {
            model.addAttribute("category", categoryService.findById(id));
            return "category/edit";
        } catch (Exception e) {
            return "redirect:/categories";
        }
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("category") Category category,
                         Model model) {
        try {
            categoryService.save(category);
            return "redirect:/categories";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "category/edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id,
                         Model model) {
        try {
            categoryService.delete(id);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", categoryService.findAll());
            return "category/list";
        }
        return "redirect:/categories";
    }
}
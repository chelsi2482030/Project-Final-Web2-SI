package com.example.productcrud.controller;

import com.example.productcrud.model.Categories;
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
        model.addAttribute("categories", new Categories());
        return "category/add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("categories") Categories categories,
                       Model model) {
        try {
            categoryService.save(categories);
            return "redirect:/categories";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "category/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        try {
            model.addAttribute("categories", categoryService.findById(id));
            return "category/edit";
        } catch (Exception e) {
            return "redirect:/categories";
        }
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("categories") Categories categories,
                         Model model) {
        try {
            categoryService.save(categories);
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
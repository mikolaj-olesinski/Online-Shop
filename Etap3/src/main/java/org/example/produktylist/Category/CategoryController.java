package org.example.produktylist.Category;

import org.example.produktylist.Service.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String listCategory (Authentication authentication, Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categoryList", categories);

        boolean isAdmin = SecurityUtils.isAdmin(authentication);
        model.addAttribute("isAdmin", isAdmin);

        return "category/index";
    }


    @GetMapping("/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/add";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category) {
        categoryService.addCategory(category);
        return "redirect:/category/";
    }

    @GetMapping("/{categoryId}/edit")
    public String editCategoryForm(@PathVariable Long categoryId, Model model) {
        Category category = categoryService.getCategoryById(categoryId);
        model.addAttribute("category", category);
        return "category/edit";
    }

    @PostMapping("/edit")
    public String editCategory(@ModelAttribute Category category) {
        categoryService.updateCategory(category);
        return "redirect:/category/";
    }

    @GetMapping("/{categoryId}/details")
    public String categoryDetails(@PathVariable Long categoryId, Model model) {
        Category category = categoryService.getCategoryById(categoryId);
        model.addAttribute("category", category);
        return "category/details";
    }

    @GetMapping("/{categoryId}/remove")
    public String removeCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return "redirect:/category/";
    }

}

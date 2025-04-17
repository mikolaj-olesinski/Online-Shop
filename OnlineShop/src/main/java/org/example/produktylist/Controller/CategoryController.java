package org.example.produktylist.Controller;

import org.example.produktylist.Entity.Category;
import org.example.produktylist.Service.CategoryService;
import org.example.produktylist.Utils.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Kontroler obsługujący żądania związane z kategoriami produktów.
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     *Metoda zwracająca listę kategorii produktów.
     * @param authentication informacje o zalogowanym użytkowniku
     * @param model model danych
     * @return nazwa widoku
     */
    @GetMapping("/")
    public String listCategory (Authentication authentication, Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categoryList", categories);

        boolean isAdmin = SecurityUtils.isAdmin(authentication);
        model.addAttribute("isAdmin", isAdmin);

        return "category/index";
    }


    /**
     * Metoda zwracająca formularz dodawania nowej kategorii.
     * @param model model danych
     * @return nazwa widoku
     */
    @GetMapping("/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/add";
    }

    /**
     * Metoda obsługująca dodawanie nowej kategorii.
     * @param category obiekt kategorii
     * @return przekierowanie na listę kategorii
     */
    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category) {
        categoryService.addCategory(category);
        return "redirect:/category/";
    }

    /**
     * Metoda zwracająca formularz edycji kategorii.
     * @param categoryId identyfikator kategorii
     * @param model model danych
     * @return nazwa widoku
     */
    @GetMapping("/{categoryId}/edit")
    public String editCategoryForm(@PathVariable Long categoryId, Model model) {
        Category category = categoryService.getCategoryById(categoryId);
        model.addAttribute("category", category);
        return "category/edit";
    }

    /**
     * Metoda obsługująca edycję kategorii.
     * @param category obiekt kategorii
     * @return przekierowanie na listę kategorii
     */
    @PostMapping("/edit")
    public String editCategory(@ModelAttribute Category category) {
        categoryService.updateCategory(category);
        return "redirect:/category/";
    }

    /**
     * Metoda zwracająca szczegóły kategorii.
     * @param categoryId identyfikator kategorii
     * @param model model danych
     * @return nazwa widoku
     */
    @GetMapping("/{categoryId}/details")
    public String categoryDetails(@PathVariable Long categoryId, Model model) {
        Category category = categoryService.getCategoryById(categoryId);
        model.addAttribute("category", category);
        return "category/details";
    }

    /**
     * Metoda obsługująca usuwanie kategorii.
     * @param categoryId identyfikator kategorii
     * @return przekierowanie na listę kategorii
     */
    @GetMapping("/{categoryId}/remove")
    public String removeCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return "redirect:/category/";
    }

}

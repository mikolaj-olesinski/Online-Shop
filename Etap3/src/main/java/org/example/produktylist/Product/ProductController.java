package org.example.produktylist.Product;

import org.example.produktylist.Category.CategoryService;
import org.example.produktylist.Service.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String listProducts(Authentication authentication, Model model) {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("productList", productList);

        boolean isAdmin = SecurityUtils.isAdmin(authentication);
        model.addAttribute("isAdmin", isAdmin);

        return "product/index";
    }

    @GetMapping("/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categoriesList", categoryService.getAllCategories());
        return "product/add";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.addProduct(product);
        return "redirect:/product/";
    }

    @GetMapping("/{productId}/edit")
    public String editProduct(@PathVariable Long productId, Model model) {
        model.addAttribute("product", productService.getProductById(productId));
        model.addAttribute("categoriesList", categoryService.getAllCategories());
        return "product/edit";
    }

    @PostMapping("/edit")
    public String editProduct(@ModelAttribute Product product) {
        if (product.getCategory() == null) {
            throw new IllegalArgumentException("Produkt musi mieć przypisaną kategorię");
        }

        productService.updateProduct(product);
        return "redirect:/product/";
    }

    @GetMapping("/{productId}/details")
    public String productDetails(@PathVariable Long productId, Model model) {
        model.addAttribute("product", productService.getProductById(productId));
        model.addAttribute("isAdmin", true); // or your actual admin check
        return "product/details";
    }

    @GetMapping("/{productId}/remove")
    public String removeProduct(@PathVariable Long productId) {
        productService.deleteProductById(productId);
        return "redirect:/product/";
    }
}

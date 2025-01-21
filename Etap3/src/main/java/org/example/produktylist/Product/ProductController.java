package org.example.produktylist.Product;

import org.example.produktylist.Category.CategoryService;
import org.example.produktylist.Comment.Comment;
import org.example.produktylist.Comment.CommentRepository;
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
    private final CommentRepository commentRepository;

    public ProductController(ProductService productService, CategoryService categoryService, CommentRepository commentRepository) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.commentRepository = commentRepository;
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
    public String productDetails(@PathVariable Long productId, Model model, Authentication authentication) {
        Product product = productService.getProductById(productId);

        List<Comment> comments = commentRepository.findByProductId(productId);

        model.addAttribute("product", product);
        model.addAttribute("comments", comments);
        boolean isAdmin = SecurityUtils.isAdmin(authentication);
        model.addAttribute("isAdmin", isAdmin);
        return "product/details";
    }

    @GetMapping("/{productId}/remove")
    public String removeProduct(@PathVariable Long productId) {
        productService.deleteProductById(productId);
        return "redirect:/product/";
    }

    @GetMapping("/{productId}/remove-comment")
    public String removeComment(@PathVariable Long productId, @RequestParam Long id) {
        // Usuwamy komentarz z bazy danych
        commentRepository.deleteById(id);
        // Po usunięciu przekierowujemy do strony szczegółów produktu
        return "redirect:/product/" + productId + "/details";
    }

}

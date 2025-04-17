package org.example.produktylist.Controller;

import org.example.produktylist.Entity.Product;
import org.example.produktylist.Service.ProductService;
import org.example.produktylist.Service.CategoryService;
import org.example.produktylist.Entity.Comment;
import org.example.produktylist.Repository.CommentRepository;
import org.example.produktylist.Utils.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler obsługujący żądania związane z produktami.
 */
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

    /**
     * Metoda zwracająca listę produktów.
     * @param authentication informacje o zalogowanym użytkowniku
     * @param model model danych
     * @return nazwa widoku
     */
    @GetMapping("/")
    public String listProducts(Authentication authentication, Model model) {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("productList", productList);

        boolean isAdmin = SecurityUtils.isAdmin(authentication);
        model.addAttribute("isAdmin", isAdmin);

        return "product/index";
    }

    /**
     * Metoda zwracająca formularz dodawania nowego produktu.
     * @param model model danych
     * @return nazwa widoku
     */
    @GetMapping("/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categoriesList", categoryService.getAllCategories());
        return "product/add";
    }

    /**
     * Metoda obsługująca dodawanie nowego produktu.
     * @param product obiekt produktu
     * @return przekierowanie na listę produktów
     */
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.addProduct(product);
        return "redirect:/product/";
    }

    /**
     * Metoda zwracająca formularz edycji produktu.
     * @param productId identyfikator produktu
     * @param model model danych
     * @return nazwa widoku
     */
    @GetMapping("/{productId}/edit")
    public String editProduct(@PathVariable Long productId, Model model) {
        model.addAttribute("product", productService.getProductById(productId));
        model.addAttribute("categoriesList", categoryService.getAllCategories());
        return "product/edit";
    }

    /**
     * Metoda obsługująca edycję produktu.
     * @param product obiekt produktu
     * @return przekierowanie na listę produktów
     */
    @PostMapping("/edit")
    public String editProduct(@ModelAttribute Product product) {
        if (product.getCategory() == null) {
            throw new IllegalArgumentException("Produkt musi mieć przypisaną kategorię");
        }

        productService.updateProduct(product);
        return "redirect:/product/";
    }

    /**
     * Metoda zwracająca szczegóły produktu.
     * @param productId identyfikator produktu
     * @param model model danych
     * @param authentication informacje o zalogowanym użytkowniku
     * @return nazwa widoku
     */
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

    /**
     * Metoda obsługująca dodawanie komentarza do produktu.
     * @param productId identyfikator produktu
     * @return przekierowanie na stronę produktu
     */
    @GetMapping("/{productId}/remove")
    public String removeProduct(@PathVariable Long productId) {
        productService.deleteProductById(productId);
        return "redirect:/product/";
    }

    /**
     * Metoda obsługująca dodawanie komentarza do produktu.
     * @param productId identyfikator produktu
     * @param id obiekt komentarza
     * @return przekierowanie na stronę produktu
     */
    @GetMapping("/{productId}/remove-comment")
    public String removeComment(@PathVariable Long productId, @RequestParam Long id) {
        commentRepository.deleteById(id);

        return "redirect:/product/" + productId + "/details";
    }

}

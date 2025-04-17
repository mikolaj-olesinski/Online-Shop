package org.example.produktylist;

import org.example.produktylist.Controller.ProductController;
import org.example.produktylist.Entity.Product;
import org.example.produktylist.Service.ProductService;
import org.example.produktylist.Service.CategoryService;
import org.example.produktylist.Repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ProductController productController;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(100.00)
                .description1("Test Description")
                .build();
    }

    @Test
    void listProducts_ShouldReturnProductList() throws Exception {
        List<Product> products = new ArrayList<>();
        products.add(testProduct);
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/product/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("productList", products))
                .andExpect(view().name("product/index"));

        verify(productService).getAllProducts();
    }

    @Test
    void addProduct_ShouldDisplayAddProductForm() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/product/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/add"));
    }

    @Test
    void addProduct_Post_ShouldRedirectToProductList() throws Exception {
        doNothing().when(productService).addProduct(any(Product.class));

        mockMvc.perform(post("/product/add")
                        .param("name", "Test Product")
                        .param("price", "100.00")
                        .param("description1", "Test Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/"));

        verify(productService).addProduct(any(Product.class));
    }

    @Test
    void editProduct_ShouldDisplayEditProductForm() throws Exception {
        when(productService.getProductById(1L)).thenReturn(testProduct);
        when(categoryService.getAllCategories()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/product/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/edit"));
    }

    @Test
    void removeProduct_ShouldRedirectToProductList() throws Exception {
        doNothing().when(productService).deleteProductById(1L);

        mockMvc.perform(get("/product/1/remove"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/"));

        verify(productService).deleteProductById(1L);
    }

    @Test
    void productDetails_ShouldShowProductDetails() throws Exception {
        when(productService.getProductById(1L)).thenReturn(testProduct);
        when(commentRepository.findByProductId(1L)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/product/1/details"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/details"))
                .andExpect(model().attribute("product", testProduct));

        verify(productService).getProductById(1L);
        verify(commentRepository).findByProductId(1L);
    }
}

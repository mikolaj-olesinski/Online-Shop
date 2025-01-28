package org.example.produktylist;
import org.example.produktylist.Entity.Category;
import org.example.produktylist.Entity.Product;
import org.example.produktylist.Repository.ProductRepository;
import org.example.produktylist.Service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;
    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);

        testCategory = Category.builder()
                .id(1L)
                .name("Test Category")
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(99.99)
                .category(testCategory)
                .description1("Test Description")
                .build();
    }

    @Test
    void getAllProducts_ReturnsAllProducts() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(
                testProduct,
                Product.builder().id(2L).name("Product 2").price(199.99).category(testCategory).build()
        );
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_ExistingProduct_ReturnsProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act
        Product result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(99.99, result.getPrice());
        assertEquals("Test Category", result.getCategory().getName());
    }

    @Test
    void getProductById_NonExistingProduct_ReturnsNull() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Product result = productService.getProductById(999L);

        // Assert
        assertNull(result);
    }

    @Test
    void addProduct_Success() {
        // Arrange
        Product newProduct = Product.builder()
                .name("New Product")
                .price(299.99)
                .category(testCategory)
                .build();
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        // Act
        productService.addProduct(newProduct);

        // Assert
        verify(productRepository).save(newProduct);
    }

    @Test
    void updateProduct_Success() {
        // Arrange
        testProduct.setName("Updated Product");
        testProduct.setPrice(149.99);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        productService.updateProduct(testProduct);

        // Assert
        verify(productRepository).save(testProduct);
    }

    @Test
    void deleteProductById_Success() {
        // Arrange
        doNothing().when(productRepository).deleteById(1L);

        // Act
        productService.deleteProductById(1L);

        // Assert
        verify(productRepository).deleteById(1L);
    }
}
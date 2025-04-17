package org.example.produktylist;

import org.example.produktylist.Entity.Product;
import org.example.produktylist.Entity.Category;
import org.example.produktylist.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");

        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(99.99)
                .category(testCategory)
                .description1("Test Description")
                .build();
    }

    @Test
    void findByName_ExistingProduct_ReturnsProduct() {
        when(productRepository.findByName("Test Product")).thenReturn(Optional.of(testProduct));

        Optional<Product> result = productRepository.findByName("Test Product");

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
        verify(productRepository).findByName("Test Product");
    }

    @Test
    void findByName_NonExistingProduct_ReturnsEmpty() {
        when(productRepository.findByName("Non Existing Product")).thenReturn(Optional.empty());

        Optional<Product> result = productRepository.findByName("Non Existing Product");

        assertFalse(result.isPresent());
        verify(productRepository).findByName("Non Existing Product");
    }
}


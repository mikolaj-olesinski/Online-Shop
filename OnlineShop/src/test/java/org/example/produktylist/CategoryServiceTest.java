package org.example.produktylist;


import org.example.produktylist.Entity.Category;
import org.example.produktylist.Entity.Product;
import org.example.produktylist.Repository.CategoryRepository;
import org.example.produktylist.Service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;
    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService(categoryRepository);

        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(99.99)
                .description1("Test Description")
                .build();

        testCategory = Category.builder()
                .id(1L)
                .name("Test Category")
                .products(new ArrayList<>(Arrays.asList(testProduct)))
                .build();
    }

    @Test
    void getAllCategories_ReturnsAllCategories() {
        // Arrange
        List<Category> expectedCategories = Arrays.asList(
                Category.builder().id(1L).name("Category 1").build(),
                Category.builder().id(2L).name("Category 2").build()
        );
        when(categoryRepository.findAll()).thenReturn(expectedCategories);

        // Act
        List<Category> result = categoryService.getAllCategories();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Category 1", result.get(0).getName());
        verify(categoryRepository).findAll();
    }

    @Test
    void getCategoryById_ExistingCategory_ReturnsCategory() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // Act
        Category result = categoryService.getCategoryById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Category", result.getName());
        assertEquals(1, result.getProducts().size());
    }

    @Test
    void getCategoryById_NonExistingCategory_ReturnsNull() {
        // Arrange
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Category result = categoryService.getCategoryById(999L);

        // Assert
        assertNull(result);
    }

    @Test
    void addCategory_Success() {
        Category newCategory = Category.builder()
                .name("New Category")
                .build();
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        categoryService.addCategory(newCategory);

        verify(categoryRepository).save(newCategory);
    }

    @Test
    void updateCategory_Success() {
        testCategory.setName("Updated Category");
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        categoryService.updateCategory(testCategory);

        verify(categoryRepository).save(testCategory);
    }

    @Test
    void deleteCategoryById_ExistingCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        categoryService.deleteCategoryById(1L);

        verify(categoryRepository).save(testCategory);
        verify(categoryRepository).delete(testCategory);
    }

    @Test
    void deleteCategoryById_NonExistingCategory_NoAction() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        categoryService.deleteCategoryById(999L);

        verify(categoryRepository, never()).delete(any());
    }
}


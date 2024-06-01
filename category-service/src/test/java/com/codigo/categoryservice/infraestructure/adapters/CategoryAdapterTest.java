package com.codigo.categoryservice.infraestructure.adapters;

import com.codigo.categoryservice.domain.aggregates.dto.CategoryDto;
import com.codigo.categoryservice.domain.aggregates.request.CategoryRequest;
import com.codigo.categoryservice.domain.aggregates.response.BaseResponse;
import com.codigo.categoryservice.infraestructure.dao.CategoryRespository;
import com.codigo.categoryservice.infraestructure.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CategoryAdapterTest {

    @Mock
    private CategoryRespository categoryRespository;

    @InjectMocks
    private CategoryAdapter categoryAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOut() {
        CategoryRequest request = CategoryRequest.builder()
                .nombre("Technology")
                .build();
        when(categoryRespository.existsByNombre(anyString())).thenReturn(true);
        ResponseEntity<BaseResponse> response = categoryAdapter.createOut(request);
        assertEquals(410, response.getBody().getCode());
        assertEquals("Category exists", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testNewCategory() {
        CategoryRequest request = CategoryRequest.builder()
                .nombre("Technology")
                .build();
        Category category = Category.builder().build();
        when(categoryRespository.existsByNombre(anyString())).thenReturn(false);
        when(categoryRespository.save(any(Category.class))).thenReturn(category);
        ResponseEntity<BaseResponse> response = categoryAdapter.createOut(request);

        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
        assertInstanceOf(CategoryDto.class, response.getBody().getResponse().get());
    }

    @Test
    void testFindById() {
        Category category = Category.builder().build();
        when(categoryRespository.findById(anyLong())).thenReturn(Optional.of(category));
        ResponseEntity<BaseResponse> response = categoryAdapter.findByIdOut(anyLong());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
        assertInstanceOf(CategoryDto.class, response.getBody().getResponse().get());
    }

    @Test
    void testFindByIdElse() {
        when(categoryRespository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = categoryAdapter.findByIdOut(anyLong());
        assertEquals(414, response.getBody().getCode());
        assertEquals("Not Found Category", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testGetAllOut() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        when(categoryRespository.findAll()).thenReturn(categories);
        ResponseEntity<BaseResponse> response = categoryAdapter.getAllOut();
        assertEquals(200, Objects.requireNonNull(response.getBody()).getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
    }

    @Test
    void testGetAllOutElse() {
        List<Category> categories = new ArrayList<>();
        when(categoryRespository.findAll()).thenReturn(categories);
        ResponseEntity<BaseResponse> response = categoryAdapter.getAllOut();
        assertEquals(414, Objects.requireNonNull(response.getBody()).getCode());
        assertEquals("Empty Category", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testUpdateOut() {
        CategoryRequest request = CategoryRequest.builder().build();
        Category category = Category.builder().build();
        when(categoryRespository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRespository.save(category)).thenReturn(category);
        ResponseEntity<BaseResponse> response = categoryAdapter.updateOut(anyLong(), request);
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
        assertEquals("USER MODIF",category.getUsuaModif());
    }

    @Test
    void testUpdateOutElse() {
        CategoryRequest request = CategoryRequest.builder().build();
        when(categoryRespository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = categoryAdapter.updateOut(anyLong(), request);
        assertEquals(414, response.getBody().getCode());
        assertEquals("Not Found Category", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testDeleteOut() {
        Category category = Category.builder().build();
        when(categoryRespository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRespository.save(category)).thenReturn(category);
        ResponseEntity<BaseResponse> response = categoryAdapter.deleteOut(anyLong());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
        assertEquals("USER DELETE",category.getUsuaDelet());
        assertFalse(category.getCondicion());
        assertInstanceOf(CategoryDto.class, response.getBody().getResponse().get());
    }

    @Test
    void testDeleteOutElse() {
        when(categoryRespository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = categoryAdapter.deleteOut(anyLong());
        assertEquals(414, response.getBody().getCode());
        assertEquals("Not Found Category", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testGetCategoryClientOut() {
        Category category = Category.builder().build();
        when(categoryRespository.findById(1L)).thenReturn(Optional.of(category));
        CategoryDto categoryDto = categoryAdapter.getCategoryClientOut(1L);
        assertEquals(category.getIdcategoria(), categoryDto.getIdcategoria());
        assertEquals(category.getNombre(), categoryDto.getNombre());
    }
}
package com.codigo.productservice.infraestructure.adapters;

import com.codigo.productservice.domain.aggregates.dto.CategoryDto;
import com.codigo.productservice.domain.aggregates.request.ProductRequest;
import com.codigo.productservice.domain.aggregates.response.BaseResponse;
import com.codigo.productservice.domain.aggregates.response.ProductResponse;
import com.codigo.productservice.infraestructure.client.CategoryClient;
import com.codigo.productservice.infraestructure.dao.ProductRepository;
import com.codigo.productservice.infraestructure.entity.Product;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ProductAdapterTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryClient categoryClient;

    @InjectMocks
    private ProductAdapter productAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOutConditionFalse() {
        ProductRequest request = ProductRequest.builder().categoria(anyLong()).build();
        CategoryDto categoryDto = CategoryDto.builder().condicion(false).build();
        when(categoryClient.getCategory(request.getCategoria())).thenReturn(categoryDto);
        ResponseEntity<BaseResponse> response = productAdapter.createOut(request);
        assertEquals(511, response.getBody().getCode());
        assertEquals("Category Inactive", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    // revisar
    @Test
    void testCreateOutConditionTrue() {
        ProductRequest request = ProductRequest.builder().categoria(1L).nombre("test product")
                .descripcion("description").stock(10).build();
        Product product = Product.builder().idproducto(1L).nombre("test product").build();
        CategoryDto categoryDto = CategoryDto.builder().idcategoria(1L).condicion(true).build();
        when(categoryClient.getCategory(request.getCategoria())).thenReturn(categoryDto);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductResponse productResponse = ProductResponse.builder().condicion(true)
                .usuaCreate("USER Admin").build();
        ResponseEntity<BaseResponse> response = productAdapter.createOut(request);
        assertEquals(200, response.getBody().getCode());
        assertEquals( "Success", response.getBody().getMessage());
        assertEquals("USER Admin", productResponse.getUsuaCreate());
        assertTrue(response.getBody().getResponse().isPresent());
    }

    @Test
    void testCreateOutNull() {
        ProductRequest request = ProductRequest.builder().categoria(anyLong()).build();
        when(categoryClient.getCategory(request.getCategoria())).thenReturn(null);
        ResponseEntity<BaseResponse> response = productAdapter.createOut(request);
        assertEquals(510, response.getBody().getCode());
        assertEquals("Not Found Category", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testFindById() {
        Product product = Product.builder().idproducto(4L).idcategoria(1L).build();
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        CategoryDto categoryDto = CategoryDto.builder().idcategoria(product.getIdcategoria()).build();
        when(categoryClient.getCategory(product.getIdcategoria())).thenReturn(categoryDto);
        ResponseEntity<BaseResponse> response = productAdapter.findByIdOut(anyLong());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
        assertInstanceOf(ProductResponse.class, response.getBody().getResponse().get());
    }

    @Test
    void testFindByIdNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = productAdapter.findByIdOut(anyLong());
        assertEquals(510, response.getBody().getCode());
        assertEquals("Not Found Category", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void testGetAllOut() {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        when(productRepository.findAll()).thenReturn(products);
        ResponseEntity<BaseResponse> response = productAdapter.getAllOut();
        assertEquals(200, Objects.requireNonNull(response.getBody()).getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
    }

    @Test
    void testGetAllOutEmpty() {
        List<Product> products = new ArrayList<>();
        when(productRepository.findAll()).thenReturn(products);
        ResponseEntity<BaseResponse> response = productAdapter.getAllOut();
        assertEquals(510, Objects.requireNonNull(response.getBody()).getCode());
        assertEquals("Not Found Category", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
        assertSame(Optional.empty(), response.getBody().getResponse());
    }

    @Test
    void testUpdateOut() {
        ProductRequest request = ProductRequest.builder().build();
        Product product = Product.builder().build();
        ProductResponse productResponse = ProductResponse.builder().usuaModif("USER-MODIF").build();
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        ResponseEntity<BaseResponse> response = productAdapter.updateOut(anyLong(), request);
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
        assertEquals("USER-MODIF",productResponse.getUsuaModif());
    }

    @Test
    void testUpdateOutNotFound() {
        ProductRequest request = ProductRequest.builder().build();
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = productAdapter.updateOut(anyLong(), request);
        assertEquals(510, response.getBody().getCode());
        assertEquals("Not Found Category", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
    }

    @Test
    void deleteOut() {
        Product product = Product.builder().build();
        ProductResponse productResponse = ProductResponse.builder().condicion(false).build();
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        ResponseEntity<BaseResponse> response = productAdapter.deleteOut(anyLong());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Success", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isPresent());
        assertFalse(productResponse.getCondicion());
        assertInstanceOf(ProductResponse.class, response.getBody().getResponse().get());
    }

    @Test
    void deleteOutNotFound() {
        ProductResponse productResponse = ProductResponse.builder().condicion(true).build();
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<BaseResponse> response = productAdapter.deleteOut(anyLong());
        assertEquals(510, response.getBody().getCode());
        assertEquals("Not Found Category", response.getBody().getMessage());
        assertTrue(response.getBody().getResponse().isEmpty());
        assertTrue(productResponse.getCondicion());
        assertSame(Optional.empty(), response.getBody().getResponse());
    }



}
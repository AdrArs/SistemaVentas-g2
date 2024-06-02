package com.codigo.productservice.application.controller;

import com.codigo.productservice.domain.aggregates.dto.ProductDto;
import com.codigo.productservice.domain.aggregates.request.ProductRequest;
import com.codigo.productservice.domain.aggregates.response.BaseResponse;
import com.codigo.productservice.domain.ports.in.ProductServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceIn productServiceIn;
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getProduct(@PathVariable Long id){
        return productServiceIn.findByIdIn(id);
    }
    @GetMapping("/find/{id}")
    public ProductDto getProductClient(@PathVariable Long id){
        return productServiceIn.getByIdIn(id);
    }
    @GetMapping
    public ResponseEntity<BaseResponse> getAll(){
        return productServiceIn.getAllIn();
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createProduct(@RequestBody ProductRequest productRequest){
        return productServiceIn.createIn(productRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest){
        return productServiceIn.updateIn(id,productRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteProduct(@PathVariable Long id){
        return productServiceIn.deleteIn(id);
    }

    @GetMapping("/updateStock/{valor}")
    void updateStock(@PathVariable String valor){
        productServiceIn.updateStockIn(valor);
    }

    @GetMapping("/resetStock/{valor}")
    void resetStock(@PathVariable String valor){
        productServiceIn.resetStockIn(valor);
    }
}

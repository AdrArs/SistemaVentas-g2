package com.codigo.venta_service.application.controller;

import com.codigo.venta_service.domain.aggregates.request.SaleRequest;
import com.codigo.venta_service.domain.aggregates.response.BaseResponse;
import com.codigo.venta_service.domain.ports.in.SaleServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sale")
@RequiredArgsConstructor
public class SaleController {

    private final SaleServiceIn saleServiceIn;

    @PostMapping
    public ResponseEntity<BaseResponse> createSale(@RequestBody SaleRequest saleRequest){
        return saleServiceIn.createIn(saleRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getSale(@PathVariable Long id){

        return saleServiceIn.findByIdIn(id);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAll(){

        return saleServiceIn.getAllIn();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateSale(@PathVariable Long id, @RequestBody SaleRequest saleRequest){
        return saleServiceIn.updateIn(id,saleRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteCategory(@PathVariable Long id){
        return saleServiceIn.deleteIn(id);
    }

}

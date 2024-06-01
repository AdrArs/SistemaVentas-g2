package com.codigo.productservice.domain.impl;

import com.codigo.productservice.domain.aggregates.request.ProductRequest;
import com.codigo.productservice.domain.aggregates.response.BaseResponse;
import com.codigo.productservice.domain.ports.in.ProductServiceIn;
import com.codigo.productservice.domain.ports.out.ProductServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductServiceIn {
    private final ProductServiceOut productServiceOut;
    @Override
    public ResponseEntity<BaseResponse> createIn(ProductRequest productRequest) {
        return productServiceOut.createOut(productRequest);
    }

    @Override
    public ResponseEntity<BaseResponse> findByIdIn(Long id) {
        return productServiceOut.findByIdOut(id);
    }

    @Override
    public ResponseEntity<BaseResponse> getAllIn() {
        return productServiceOut.getAllOut();
    }

    @Override
    public ResponseEntity<BaseResponse> updateIn(Long id, ProductRequest productRequest) {
        return productServiceOut.updateOut(id,productRequest);
    }

    @Override
    public ResponseEntity<BaseResponse> deleteIn(Long id) {
        return productServiceOut.deleteOut(id);
    }
}

package com.codigo.productservice.domain.ports.out;

import com.codigo.productservice.domain.aggregates.request.ProductRequest;
import com.codigo.productservice.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface ProductServiceOut {
    ResponseEntity<BaseResponse> createOut(ProductRequest productRequest);
    ResponseEntity<BaseResponse> findByIdOut(Long id);
    ResponseEntity<BaseResponse> getAllOut();
    ResponseEntity<BaseResponse> updateOut(Long id, ProductRequest productRequest);
    ResponseEntity<BaseResponse> deleteOut(Long id);
}

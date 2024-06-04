package com.codigo.productservice.domain.ports.in;

import com.codigo.productservice.domain.aggregates.dto.ProductDto;
import com.codigo.productservice.domain.aggregates.request.ProductRequest;
import com.codigo.productservice.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;


public interface ProductServiceIn {
    ResponseEntity<BaseResponse> createIn(ProductRequest productRequest);
    ResponseEntity<BaseResponse> findByIdIn(Long id);
    ResponseEntity<BaseResponse> getAllIn();
    ResponseEntity<BaseResponse> updateIn(Long id, ProductRequest productRequest);
    ResponseEntity<BaseResponse> deleteIn(Long id);

    ProductDto getByIdIn(Long id);

    void updateStockIn(String valor);

    void resetStockIn(String valor);
}

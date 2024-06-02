package com.codigo.venta_service.domain.ports.in;

import com.codigo.venta_service.domain.aggregates.request.SaleRequest;
import com.codigo.venta_service.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface SaleServiceIn {
    ResponseEntity<BaseResponse> createIn(SaleRequest saleRequest);
    ResponseEntity<BaseResponse> findByIdIn(Long id);
    ResponseEntity<BaseResponse> getAllIn();
    ResponseEntity<BaseResponse> updateIn(Long id, SaleRequest saleRequest);
    ResponseEntity<BaseResponse> deleteIn(Long id);


}

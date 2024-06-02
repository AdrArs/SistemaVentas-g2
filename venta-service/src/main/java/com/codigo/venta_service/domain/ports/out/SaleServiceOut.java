package com.codigo.venta_service.domain.ports.out;

import com.codigo.venta_service.domain.aggregates.request.SaleRequest;
import com.codigo.venta_service.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface SaleServiceOut {
    ResponseEntity<BaseResponse> createOut(SaleRequest saleRequest);
    ResponseEntity<BaseResponse> findByIdOut(Long id);
    ResponseEntity<BaseResponse> getAllOut();
    ResponseEntity<BaseResponse> updateOut(Long id, SaleRequest saleRequest);
    ResponseEntity<BaseResponse> deleteOut(Long id);
}

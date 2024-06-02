package com.codigo.venta_service.domain.impl;

import com.codigo.venta_service.domain.aggregates.request.SaleRequest;
import com.codigo.venta_service.domain.aggregates.response.BaseResponse;
import com.codigo.venta_service.domain.ports.in.SaleServiceIn;
import com.codigo.venta_service.domain.ports.out.SaleServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleServiceIn {
    private final SaleServiceOut saleServiceOut;


    @Override
    public ResponseEntity<BaseResponse> createIn(SaleRequest saleRequest) {
        return saleServiceOut.createOut(saleRequest);
    }

    @Override
    public ResponseEntity<BaseResponse> findByIdIn(Long id) {
        return saleServiceOut.findByIdOut(id);
    }

    @Override
    public ResponseEntity<BaseResponse> getAllIn() {
        return saleServiceOut.getAllOut();
    }

    @Override
    public ResponseEntity<BaseResponse> updateIn(Long id, SaleRequest saleRequest) {
        return saleServiceOut.updateOut(id,saleRequest);
    }

    @Override
    public ResponseEntity<BaseResponse> deleteIn(Long id) {
        return saleServiceOut.deleteOut(id);
    }
}

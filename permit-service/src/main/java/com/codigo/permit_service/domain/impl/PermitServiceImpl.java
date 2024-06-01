package com.codigo.permit_service.domain.impl;

import com.codigo.permit_service.domain.aggregates.request.PermitRequest;
import com.codigo.permit_service.domain.aggregates.response.BaseResponse;
import com.codigo.permit_service.domain.ports.in.PermitServiceIn;
import com.codigo.permit_service.domain.ports.out.PermitServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermitServiceImpl implements PermitServiceIn {
    private final PermitServiceOut permitServiceOut;
    @Override
    public ResponseEntity<BaseResponse> createIn(PermitRequest permitRequest) {
        return permitServiceOut.createOut(permitRequest);
    }

    @Override
    public ResponseEntity<BaseResponse> findByIdIn(Long id) {
        return permitServiceOut.findByIdOut(id);
    }

    @Override
    public ResponseEntity<BaseResponse> getAllIn() {
        return permitServiceOut.getAllOut();
    }

    @Override
    public ResponseEntity<BaseResponse> updateIn(Long id, PermitRequest permitRequest) {
        return permitServiceOut.updateOut(id,permitRequest);
    }
}

package com.codigo.permit_service.domain.impl;

import com.codigo.permit_service.domain.aggregates.request.PermitPersonRequest;
import com.codigo.permit_service.domain.aggregates.response.BaseResponse;
import com.codigo.permit_service.domain.ports.in.PermitPersonIn;
import com.codigo.permit_service.domain.ports.out.PermitPersonOut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermitPersonImpl implements PermitPersonIn {
    private final PermitPersonOut permitPersonOut;
    @Override
    public ResponseEntity<BaseResponse> createIn(PermitPersonRequest permitPersonRequest) {
        return permitPersonOut.createOut(permitPersonRequest);
    }

    @Override
    public ResponseEntity<BaseResponse> findByIdIn(Long id) {
        return permitPersonOut.findByIdOut(id);
    }

    @Override
    public ResponseEntity<BaseResponse> findPersonByIdIn(Long id) {
        return permitPersonOut.findPersonByIdOut(id);
    }

    @Override
    public ResponseEntity<BaseResponse> getAllIn() {
        return permitPersonOut.getAllOut();
    }

    @Override
    public ResponseEntity<BaseResponse> updateIn(Long id, PermitPersonRequest permitPersonRequest) {
        return permitPersonOut.updateOut(id,permitPersonRequest);
    }
}

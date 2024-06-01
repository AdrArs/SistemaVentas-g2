package com.codigo.permit_service.domain.ports.in;

import com.codigo.permit_service.domain.aggregates.request.PermitRequest;
import com.codigo.permit_service.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface PermitServiceIn {
    ResponseEntity<BaseResponse> createIn(PermitRequest permitRequest);
    ResponseEntity<BaseResponse> findByIdIn(Long id);
    ResponseEntity<BaseResponse> getAllIn();
    ResponseEntity<BaseResponse> updateIn(Long id, PermitRequest permitRequest);
}

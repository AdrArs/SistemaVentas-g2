package com.codigo.permit_service.domain.ports.out;

import com.codigo.permit_service.domain.aggregates.request.PermitRequest;
import com.codigo.permit_service.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface PermitServiceOut {
    ResponseEntity<BaseResponse> createOut(PermitRequest permitRequest);
    ResponseEntity<BaseResponse> findByIdOut(Long id);
    ResponseEntity<BaseResponse> getAllOut();
    ResponseEntity<BaseResponse> updateOut(Long id, PermitRequest permitRequest);
}

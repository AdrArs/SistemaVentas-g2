package com.codigo.permit_service.domain.ports.out;

import com.codigo.permit_service.domain.aggregates.request.PermitPersonRequest;
import com.codigo.permit_service.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface PermitPersonOut {
    ResponseEntity<BaseResponse> createOut(PermitPersonRequest permitPersonRequest);
    ResponseEntity<BaseResponse> findByIdOut(Long id);
    ResponseEntity<BaseResponse> findPersonByIdOut(Long id);
    ResponseEntity<BaseResponse> getAllOut();
    ResponseEntity<BaseResponse> updateOut(Long id, PermitPersonRequest permitPersonRequest);
}

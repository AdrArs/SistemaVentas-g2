package com.codigo.permit_service.domain.ports.in;

import com.codigo.permit_service.domain.aggregates.request.PermitPersonRequest;
import com.codigo.permit_service.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface PermitPersonIn {
    ResponseEntity<BaseResponse> createIn(PermitPersonRequest permitPersonRequest);
    ResponseEntity<BaseResponse> findByIdIn(Long id);

    ResponseEntity<BaseResponse> findPersonByIdIn(Long id);
    ResponseEntity<BaseResponse> getAllIn();
    ResponseEntity<BaseResponse> updateIn(Long id, PermitPersonRequest permitPersonRequest);
}

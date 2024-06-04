package com.codigo.personservice.domain.ports.out;

import com.codigo.personservice.domain.aggregates.dto.CustomerDto;
import com.codigo.personservice.domain.aggregates.dto.OperatorDto;
import com.codigo.personservice.domain.aggregates.dto.PersonDto;
import com.codigo.personservice.domain.aggregates.request.PersonRequest;
import com.codigo.personservice.domain.aggregates.request.SupplierRequest;
import com.codigo.personservice.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface PersonServiceOut {
    ResponseEntity<BaseResponse> createSupplierOut(SupplierRequest supplierRequest);
    ResponseEntity<BaseResponse> createCustomerOrOperatorOut(PersonRequest personRequest , String tipo);
    ResponseEntity<BaseResponse> findByNumDocOut(String num);
    ResponseEntity<BaseResponse> getAllSupplierOut();
    ResponseEntity<BaseResponse> getAllCustomerOut();
    ResponseEntity<BaseResponse> getAllOperatorOut();
    ResponseEntity<BaseResponse> findCustomerByNumDocOut(String num);
    ResponseEntity<BaseResponse> findOperatorByNumDocOut(String num);
    ResponseEntity<BaseResponse> findSupplierByNumDocOut(String num);
    ResponseEntity<BaseResponse> updateCustomerOrOperatorIn(Long id,PersonRequest personRequest, String tipo);

    PersonDto getPersonClientOut(Long id);

    OperatorDto getOperatorClientOut(Long id);

    CustomerDto getCustomerClientOut(Long id);

}

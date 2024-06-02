package com.codigo.personservice.domain.ports.in;

import com.codigo.personservice.domain.aggregates.dto.CustomerDto;
import com.codigo.personservice.domain.aggregates.dto.OperatorDto;
import com.codigo.personservice.domain.aggregates.dto.PersonDto;
import com.codigo.personservice.domain.aggregates.request.PersonRequest;
import com.codigo.personservice.domain.aggregates.request.SupplierRequest;
import com.codigo.personservice.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface PersonServiceIn {
    ResponseEntity<BaseResponse> createSupplierIn(SupplierRequest supplierRequest);
    ResponseEntity<BaseResponse> createCustomerOrOperatorIn(PersonRequest personRequest , String tipo);
    ResponseEntity<BaseResponse> findCustomerByNumDocIn(String num);
    ResponseEntity<BaseResponse> findOperatorByNumDocIn(String num);
    ResponseEntity<BaseResponse> findSupplierByNumDocIn(String num);
    ResponseEntity<BaseResponse> findByNumDocIn(String num);
    ResponseEntity<BaseResponse> getAllSupplierIn();
    ResponseEntity<BaseResponse> getAllCustomerIn();
    ResponseEntity<BaseResponse> getAllOperatorIn();
    
    ResponseEntity<BaseResponse> updateCustomerOrOperatorIn(Long id,PersonRequest personRequest, String tipo);
//    ResponseEntity<BaseResponse> deleteIn(Long id);

    PersonDto getPersonClientIn(Long id);

    OperatorDto getOperatorClientIn(Long id);

    CustomerDto getCustomerClientIn(Long id);

}

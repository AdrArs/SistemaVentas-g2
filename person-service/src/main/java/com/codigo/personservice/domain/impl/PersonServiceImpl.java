package com.codigo.personservice.domain.impl;

import com.codigo.personservice.domain.aggregates.dto.PersonDto;
import com.codigo.personservice.domain.aggregates.request.PersonRequest;
import com.codigo.personservice.domain.aggregates.request.SupplierRequest;
import com.codigo.personservice.domain.aggregates.response.BaseResponse;
import com.codigo.personservice.domain.ports.in.PersonServiceIn;
import com.codigo.personservice.domain.ports.out.PersonServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonServiceIn {
    private final PersonServiceOut personServiceOut;

    @Override
    public ResponseEntity<BaseResponse> createSupplierIn(SupplierRequest supplierRequest) {
        return personServiceOut.createSupplierOut(supplierRequest);
    }

    @Override
    public ResponseEntity<BaseResponse> createCustomerOrOperatorIn(PersonRequest personRequest, String tipo) {
        return personServiceOut.createCustomerOrOperatorOut(personRequest,tipo);
    }

    @Override
    public ResponseEntity<BaseResponse> findCustomerByNumDocIn(String num) {
        return personServiceOut.findCustomerByNumDocOut(num);
    }

    @Override
    public ResponseEntity<BaseResponse> findOperatorByNumDocIn(String num) {
        return personServiceOut.findOperatorByNumDocOut(num);
    }

    @Override
    public ResponseEntity<BaseResponse> findSupplierByNumDocIn(String num) {
        return personServiceOut.findSupplierByNumDocOut(num);
    }

    @Override
    public ResponseEntity<BaseResponse> findByNumDocIn(String num) {
        return personServiceOut.findByNumDocOut(num);
    }

    @Override
    public ResponseEntity<BaseResponse> getAllSupplierIn() {
        return personServiceOut.getAllSupplierOut();
    }

    @Override
    public ResponseEntity<BaseResponse> getAllCustomerIn() {
        return personServiceOut.getAllCustomerOut();
    }

    @Override
    public ResponseEntity<BaseResponse> getAllOperatorIn() {
        return personServiceOut.getAllOperatorOut();
    }

    @Override
    public ResponseEntity<BaseResponse> updateCustomerOrOperatorIn(Long id, PersonRequest personRequest, String tipo ) {
        return personServiceOut.updateCustomerOrOperatorIn(id,personRequest,tipo);
    }

    @Override
    public PersonDto getPersonClientIn(Long id) {
        return personServiceOut.getPersonClientOut(id);
    }
}

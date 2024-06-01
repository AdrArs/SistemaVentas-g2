package com.codigo.personservice.application.controller;

import com.codigo.personservice.domain.aggregates.dto.PersonDto;
import com.codigo.personservice.domain.aggregates.request.PersonRequest;
import com.codigo.personservice.domain.aggregates.request.SupplierRequest;
import com.codigo.personservice.domain.aggregates.response.BaseResponse;
import com.codigo.personservice.domain.ports.in.PersonServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonServiceIn personServiceIn;

    @GetMapping("/supplier")
    public ResponseEntity<BaseResponse> getAllSupplier(){
        return personServiceIn.getAllSupplierIn();
    }

    @PostMapping("/supplier")
    public ResponseEntity<BaseResponse> createSupplier(@RequestBody SupplierRequest supplierRequest){
        return personServiceIn.createSupplierIn(supplierRequest);
    }
    @GetMapping("/customer")
    public ResponseEntity<BaseResponse> getAllCustomer(){
        return personServiceIn.getAllCustomerIn();
    }
    @PostMapping("/customer")
    public ResponseEntity<BaseResponse> createCustomer(@RequestBody PersonRequest personRequest){
        return personServiceIn.createCustomerOrOperatorIn(personRequest,"CUSTOMER");
    }
    @GetMapping("/operator")
    public ResponseEntity<BaseResponse> getAllOperator(){
        return personServiceIn.getAllOperatorIn();
    }
    @PostMapping("/operator")
    public ResponseEntity<BaseResponse> createOperator(@RequestBody PersonRequest personRequest){
        return personServiceIn.createCustomerOrOperatorIn(personRequest,"OPERATOR");
    }
    @GetMapping("/{numDoc}")
    public ResponseEntity<BaseResponse> getPersonByNumDoc(@PathVariable String numDoc){
        return personServiceIn.findByNumDocIn(numDoc);
    }

    @GetMapping("/customer/{numDoc}")
    public ResponseEntity<BaseResponse> getCustomerByNumDoc(@PathVariable String numDoc){
        return personServiceIn.findCustomerByNumDocIn(numDoc);
    }

    @GetMapping("/operator/{numDoc}")
    public ResponseEntity<BaseResponse> getOperatorByNumDoc(@PathVariable String numDoc){
        return personServiceIn.findOperatorByNumDocIn(numDoc);
    }

    @PutMapping("/operator/{id}")
    public ResponseEntity<BaseResponse> updateOperatorByNumDoc(@PathVariable Long id,@RequestBody PersonRequest personRequest){
        return personServiceIn.updateCustomerOrOperatorIn(id,personRequest,"OPERATOR");
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<BaseResponse> updateCustomerByNumDoc(@PathVariable Long id,@RequestBody PersonRequest personRequest){
        return personServiceIn.updateCustomerOrOperatorIn(id,personRequest,"CUSTOMER");
    }

    @GetMapping("/getPersonClient/{id}")
    public PersonDto getPersonClient(@PathVariable Long id){
        return personServiceIn.getPersonClientIn(id);
    }

}

package com.codigo.permit_service.application.controller;

import com.codigo.permit_service.domain.aggregates.request.PermitPersonRequest;
import com.codigo.permit_service.domain.aggregates.response.BaseResponse;
import com.codigo.permit_service.domain.ports.in.PermitPersonIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permit-person")
@RequiredArgsConstructor
public class PermitPersonController {
    private final PermitPersonIn permitPersonIn;
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getPermitPerson(@PathVariable Long id){
        return permitPersonIn.findByIdIn(id);
    }

    @GetMapping("person/{id}")
    public ResponseEntity<BaseResponse> getPermitByPersonId(@PathVariable Long id){
        return permitPersonIn.findPersonByIdIn(id);
    }
    @GetMapping
    public ResponseEntity<BaseResponse> getAll(){
        return permitPersonIn.getAllIn();
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createPermit(@RequestBody PermitPersonRequest permitPersonRequest){
        return permitPersonIn.createIn(permitPersonRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updatePermitPerson(@PathVariable Long id, @RequestBody PermitPersonRequest permitPersonRequest){
        return permitPersonIn.updateIn(id,permitPersonRequest);
    }
}

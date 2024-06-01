package com.codigo.permit_service.application.controller;

import com.codigo.permit_service.domain.aggregates.request.PermitRequest;
import com.codigo.permit_service.domain.aggregates.response.BaseResponse;
import com.codigo.permit_service.domain.ports.in.PermitServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/permit")
@RequiredArgsConstructor
public class PermitController {
    private final PermitServiceIn permitServiceIn;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getPermit(@PathVariable Long id){
        return permitServiceIn.findByIdIn(id);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAll(){
        return permitServiceIn.getAllIn();
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createPermit(@RequestBody PermitRequest permitRequest){
        return permitServiceIn.createIn(permitRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updatePermit(@PathVariable Long id, @RequestBody PermitRequest permitRequest){
        return permitServiceIn.updateIn(id,permitRequest);
    }
}

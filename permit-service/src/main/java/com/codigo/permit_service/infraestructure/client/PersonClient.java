package com.codigo.permit_service.infraestructure.client;

import com.codigo.permit_service.domain.aggregates.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "person-service",url = "http://localhost:8085/api/person/")
public interface PersonClient {
    @GetMapping("/getPersonClient/{id}")
    PersonDto getPerson(@PathVariable Long id);
}

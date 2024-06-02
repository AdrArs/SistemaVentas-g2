package com.codigo.venta_service.infraestructure.client;

import com.codigo.venta_service.domain.aggregates.dto.CustomerDto;
import com.codigo.venta_service.domain.aggregates.dto.OperatorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "person-client", url = "http://localhost:8085/api/person")
public interface PersonClient {
    @GetMapping("/getOperatorClient/{id}")
    OperatorDto getOperatorClient(@PathVariable Long id);

    @GetMapping("/getCustomerClient/{id}")
    CustomerDto getCustomerClient(@PathVariable Long id);

}

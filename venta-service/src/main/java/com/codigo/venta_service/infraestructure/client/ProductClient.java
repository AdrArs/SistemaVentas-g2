package com.codigo.venta_service.infraestructure.client;

import com.codigo.venta_service.domain.aggregates.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "product-client",url = "http://localhost:8082/api/product")
public interface ProductClient {

    @GetMapping("/find/{id}")
    ProductDto getProductClient(@PathVariable Long id);

    @GetMapping("/updateStock/{valor}")
    void updateStock(@PathVariable String valor);

    @GetMapping("/resetStock/{valor}")
    void resetStock(@PathVariable String valor);
}

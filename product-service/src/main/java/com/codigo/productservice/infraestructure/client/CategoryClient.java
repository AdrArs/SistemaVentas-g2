package com.codigo.productservice.infraestructure.client;

import com.codigo.productservice.domain.aggregates.dto.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "category-service",url = "http://localhost:8081/api/category/")
public interface CategoryClient {
    @GetMapping("/getCategoryClient/{id}")
    CategoryDto getCategory(@PathVariable Long id);
}

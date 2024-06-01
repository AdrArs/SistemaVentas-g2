package com.codigo.categoryservice.domain.ports.out;

import com.codigo.categoryservice.domain.aggregates.dto.CategoryDto;
import com.codigo.categoryservice.domain.aggregates.request.CategoryRequest;
import com.codigo.categoryservice.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryServiceOut {
    ResponseEntity<BaseResponse> createOut(CategoryRequest categoryRequest);
    ResponseEntity<BaseResponse> findByIdOut(Long id);
    ResponseEntity<BaseResponse> getAllOut();
    ResponseEntity<BaseResponse> updateOut(Long id, CategoryRequest categoryRequest);
    ResponseEntity<BaseResponse> deleteOut(Long id);
    CategoryDto getCategoryClientOut(Long id);
}

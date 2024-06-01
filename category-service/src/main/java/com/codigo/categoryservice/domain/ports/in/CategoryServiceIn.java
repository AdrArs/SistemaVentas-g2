package com.codigo.categoryservice.domain.ports.in;

import com.codigo.categoryservice.domain.aggregates.dto.CategoryDto;
import com.codigo.categoryservice.domain.aggregates.request.CategoryRequest;
import com.codigo.categoryservice.domain.aggregates.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryServiceIn {
    ResponseEntity<BaseResponse> createIn(CategoryRequest categoryRequest);
    ResponseEntity<BaseResponse> findByIdIn(Long id);
    ResponseEntity<BaseResponse> getAllIn();
    ResponseEntity<BaseResponse> updateIn(Long id, CategoryRequest categoryRequest);
    ResponseEntity<BaseResponse> deleteIn(Long id);

    CategoryDto getCategoryClientIn(Long id);
}

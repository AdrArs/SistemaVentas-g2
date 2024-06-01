package com.codigo.categoryservice.domain.impl;

import com.codigo.categoryservice.domain.aggregates.dto.CategoryDto;
import com.codigo.categoryservice.domain.aggregates.request.CategoryRequest;
import com.codigo.categoryservice.domain.aggregates.response.BaseResponse;
import com.codigo.categoryservice.domain.ports.in.CategoryServiceIn;
import com.codigo.categoryservice.domain.ports.out.CategoryServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryServiceIn {
    private final CategoryServiceOut categoryServiceOut;
    @Override
    public ResponseEntity<BaseResponse> createIn(CategoryRequest categoryRequest) {
        return categoryServiceOut.createOut(categoryRequest);
    }

    @Override
    public ResponseEntity<BaseResponse> findByIdIn(Long id) {
        return categoryServiceOut.findByIdOut(id);
    }

    @Override
    public ResponseEntity<BaseResponse> getAllIn() {
        return categoryServiceOut.getAllOut();
    }

    @Override
    public ResponseEntity<BaseResponse> updateIn(Long id, CategoryRequest categoryRequest) {
        return categoryServiceOut.updateOut(id,categoryRequest);
    }

    @Override
    public ResponseEntity<BaseResponse> deleteIn(Long id) {
        return categoryServiceOut.deleteOut(id);
    }

    @Override
    public CategoryDto getCategoryClientIn(Long id) {
        return categoryServiceOut.getCategoryClientOut(id);
    }

}

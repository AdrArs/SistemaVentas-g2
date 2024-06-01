package com.codigo.categoryservice.application.controller;

import com.codigo.categoryservice.domain.aggregates.dto.CategoryDto;
import com.codigo.categoryservice.domain.aggregates.request.CategoryRequest;
import com.codigo.categoryservice.domain.aggregates.response.BaseResponse;
import com.codigo.categoryservice.domain.ports.in.CategoryServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceIn categoryServiceIn;
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getCategory(@PathVariable Long id){
        return categoryServiceIn.findByIdIn(id);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAll(){
        return categoryServiceIn.getAllIn();
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createCategory(@RequestBody CategoryRequest categoryRequest){
        return categoryServiceIn.createIn(categoryRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest){
        return categoryServiceIn.updateIn(id,categoryRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteCategory(@PathVariable Long id){
        return categoryServiceIn.deleteIn(id);
    }

    @GetMapping("/getCategoryClient/{id}")
    public CategoryDto getCategoryClient(@PathVariable Long id){
        return categoryServiceIn.getCategoryClientIn(id);
    }


    @PostMapping("/validate2")
    public boolean validate2(@RequestParam("token") String token){
        return true;
    }

}

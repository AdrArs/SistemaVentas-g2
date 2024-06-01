package com.codigo.categoryservice.infraestructure.adapters;

import com.codigo.categoryservice.domain.aggregates.dto.CategoryDto;
import com.codigo.categoryservice.domain.aggregates.request.CategoryRequest;
import com.codigo.categoryservice.domain.aggregates.response.BaseResponse;
import com.codigo.categoryservice.domain.ports.out.CategoryServiceOut;
import com.codigo.categoryservice.infraestructure.dao.CategoryRespository;
import com.codigo.categoryservice.infraestructure.entity.Category;
import com.codigo.categoryservice.infraestructure.mapper.ConvertCategoryToDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryAdapter implements CategoryServiceOut {

    private final CategoryRespository categoryRespository;
    @Override
    public ResponseEntity<BaseResponse> createOut(CategoryRequest categoryRequest) {
        boolean exists = categoryRespository.existsByNombre(categoryRequest.getNombre());
        if(exists){
            return ResponseEntity
                    .ok(new BaseResponse(410,"Category exists", Optional.empty()));
        }else{
            Category category = Category.builder()
                    .nombre(categoryRequest.getNombre())
                    .descripcion(categoryRequest.getDescripcion())
                    .condicion(true)
                    .build();
            category.setUsuaCreate("Admin");
            category.setDateCreate(getTime());
            try{
                CategoryDto categoryDto = ConvertCategoryToDto.convertToDto(categoryRespository.save(category));
                return ResponseEntity
                        .ok(new BaseResponse(200,"Success", Optional.of(categoryDto)));
            }catch (Exception e){
//                controlar error generado por desconexion en bd
                return null;
            }
        }
    }

    @Override
    public ResponseEntity<BaseResponse> findByIdOut(Long id) {
        try {
            Optional<Category> category = categoryRespository.findById(id);
            if(category.isPresent()){
                CategoryDto categoryDto = ConvertCategoryToDto.convertToDto(category.get());
                return ResponseEntity
                        .ok(new BaseResponse(200,"Success", Optional.of(categoryDto)));
            }else {
                return ResponseEntity
                        .ok(new BaseResponse(414,"Not Found Category", Optional.empty()));
            }
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public ResponseEntity<BaseResponse> getAllOut() {
//        try {
            List<Category> category = categoryRespository.findAll();
            if(!category.isEmpty()){
                List<CategoryDto> categoryDto = ConvertCategoryToDto.convertListToDto(category);
                return ResponseEntity
                        .ok(new BaseResponse(200,"Success", Optional.of(categoryDto)));
            }else {
                return ResponseEntity
                        .ok(new BaseResponse(414,"Empty ategory", Optional.empty()));
            }
//        }catch (Exception e){
//            return null;
//        }
    }

    @Override
    public ResponseEntity<BaseResponse> updateOut(Long id, CategoryRequest categoryRequest) {
        try{
            Optional<Category> category = categoryRespository.findById(id);
            if(category.isPresent()){
                Category c = category.get();
                c.setNombre(categoryRequest.getNombre());
                c.setDescripcion(categoryRequest.getDescripcion());
                c.setUsuaModif("USER MODIF");
                c.setDateModif(getTime());
                CategoryDto categoryDto = ConvertCategoryToDto.convertToDto(categoryRespository.save(c));
                return ResponseEntity
                        .ok(new BaseResponse(200,"Success", Optional.of(categoryDto)));
            }else {
                return ResponseEntity
                        .ok(new BaseResponse(414,"Not Found Category", Optional.empty()));
            }
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public ResponseEntity<BaseResponse> deleteOut(Long id) {
        try {
            Optional<Category> category = categoryRespository.findById(id);
            if(category.isPresent()){
                Category c = category.get();
                c.setCondicion(false);
                c.setUsuaDelet("USER DELETE");
                c.setDateDelet(getTime());
                CategoryDto categoryDto = ConvertCategoryToDto.convertToDto(categoryRespository.save(c));
                return ResponseEntity
                        .ok(new BaseResponse(200,"Success", Optional.of(categoryDto)));
            }else{
                return ResponseEntity
                        .ok(new BaseResponse(414,"Not Found Category", Optional.empty()));
            }
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public CategoryDto getCategoryClientOut(Long id) {
        try{
            Optional<Category> category = categoryRespository.findById(id);
            if (category.isPresent()){
                CategoryDto categoryDto = ConvertCategoryToDto.convertToDto(category.get());
                return categoryDto;
            }
            return new CategoryDto();
        }catch (Exception e){
            return new CategoryDto();
        }
    }

    private Timestamp getTime(){
        long currentTime =System.currentTimeMillis();
        return new Timestamp(currentTime);
    }
}

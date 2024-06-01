package com.codigo.categoryservice.infraestructure.mapper;

import com.codigo.categoryservice.domain.aggregates.dto.CategoryDto;
import com.codigo.categoryservice.infraestructure.entity.Category;

import java.util.List;

public class ConvertCategoryToDto {

    public static CategoryDto convertToDto(Category category){
        return CategoryDto.builder()
                .idcategoria(category.getIdcategoria())
                .nombre(category.getNombre())
                .descripcion(category.getDescripcion())
                .condicion(category.getCondicion())
                .usuaCreate(category.getUsuaCreate())
                .dateCreate(category.getDateCreate())
                .usuaModif(category.getUsuaModif())
                .dateModif(category.getDateModif())
                .usuaDelet(category.getUsuaDelet())
                .dateDelet(category.getDateDelet())
                .build();
    }

    public static List<CategoryDto> convertListToDto(List<Category> category){
        return category
                .stream()
                .map(ConvertCategoryToDto::convertToDto)
                .toList();
    }

}

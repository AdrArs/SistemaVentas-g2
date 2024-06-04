package com.codigo.productservice.infraestructure.mapper;

import com.codigo.productservice.domain.aggregates.dto.CategoryDto;
import com.codigo.productservice.domain.aggregates.response.ProductResponse;
import com.codigo.productservice.infraestructure.entity.Product;

import java.util.List;

public class ConvertProduct {

    public static ProductResponse toProductResponse(Product product , CategoryDto categoryDto){
        return ProductResponse.builder()
                .categoria(categoryDto)
                .stock(product.getStock())
                .codigo(product.getCodigo())
                .condicion(product.getCondicion())
                .imagen(product.getImagen())
                .descripcion(product.getDescripcion())
                .precio(product.getPrecio())
                .nombre(product.getNombre())
                .idproducto(product.getIdproducto())
                .usuaCreate(product.getUsuaCreate())
                .dateCreate(product.getDateCreate())
                .usuaModif(product.getUsuaModif())
                .dateModif(product.getDateModif())
                .usuaDelet(product.getUsuaDelet())
                .dateDelet(product.getDateDelet())
                .build();
    }

    public static List<ProductResponse> toListProductResponse(){
        return null;
    }

}

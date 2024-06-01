package com.codigo.categoryservice.domain.aggregates.request;

import lombok.*;

@Getter
@Setter
@Builder
public class CategoryRequest {
    private String nombre;
    private String descripcion;
}

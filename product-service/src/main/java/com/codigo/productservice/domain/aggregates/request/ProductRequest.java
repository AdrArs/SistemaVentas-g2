package com.codigo.productservice.domain.aggregates.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductRequest {
    private Long categoria;
    private String codigo;
    private String nombre;
    private Integer stock;
    private String descripcion;
    private Float precio;
    private String imagen;
}

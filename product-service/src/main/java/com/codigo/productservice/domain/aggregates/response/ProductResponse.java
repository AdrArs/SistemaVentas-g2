package com.codigo.productservice.domain.aggregates.response;

import com.codigo.productservice.domain.aggregates.dto.CategoryDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@Builder
public class ProductResponse {
    private Long idproducto;
    private String codigo;
    private String nombre;
    private CategoryDto categoria;
    private Integer stock;
    private Float precio;
    private String descripcion;
    private String imagen;
    private Boolean condicion;
    private String usuaCreate;
    private Timestamp dateCreate;
    private String usuaModif;
    private Timestamp dateModif;
    private String usuaDelet;
    private Timestamp dateDelet;
}

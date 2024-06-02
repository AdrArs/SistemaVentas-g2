package com.codigo.productservice.domain.aggregates.dto;

import lombok.*;

import java.sql.Timestamp;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long idproducto;
    private Long categoria;
    private String codigo;
    private String nombre;
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

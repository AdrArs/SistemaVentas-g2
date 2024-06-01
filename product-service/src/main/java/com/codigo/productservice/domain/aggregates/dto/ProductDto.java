package com.codigo.productservice.domain.aggregates.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@Builder
public class ProductDto {
    private Long idproducto;
    private Long categoria;
    private String codigo;
    private String nombre;
    private Integer stock;
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

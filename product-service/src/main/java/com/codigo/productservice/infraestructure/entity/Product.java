package com.codigo.productservice.infraestructure.entity;

import com.codigo.productservice.infraestructure.entity.audit.Audit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idproducto;
//    @ManyToOne
//    @JoinColumn(name = "idcategoria")
    private Long idcategoria;
    private String codigo;
    private String nombre;
    private Integer stock;
    private String descripcion;
    private String imagen;
    private Boolean condicion;
}

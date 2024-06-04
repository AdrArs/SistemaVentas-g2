package com.codigo.venta_service.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalleVenta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleVenta;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idventa")
    private Sale venta;
    private Long idproducto;
    private Integer cantidad;
    private Float precioVenta;
    private Float descuento;
}

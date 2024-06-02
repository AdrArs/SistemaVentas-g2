package com.codigo.venta_service.domain.aggregates.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDetailDto {
    private Long idDetalleVenta;
    private Long idproducto;
    private Integer cantidad;
    private Float precioVenta;
    private Float descuento;
}

package com.codigo.venta_service.domain.aggregates.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleRequest {
    private Long idproducto;
    private Integer cantidad;
}

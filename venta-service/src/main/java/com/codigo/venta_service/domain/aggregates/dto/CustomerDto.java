package com.codigo.venta_service.domain.aggregates.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Long id;
    private String codCliente;
    private PersonDto persona;
}

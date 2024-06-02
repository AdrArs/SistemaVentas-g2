package com.codigo.venta_service.domain.aggregates.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperatorDto {
    private Long id;
    private String codEmpleado;
    private PersonDto persona;
}

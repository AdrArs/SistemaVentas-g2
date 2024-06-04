package com.codigo.permit_service.domain.aggregates.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermitDto {
    private Long idpermiso;
    private String permiso;
}

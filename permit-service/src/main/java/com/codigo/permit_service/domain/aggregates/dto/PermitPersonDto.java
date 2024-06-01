package com.codigo.permit_service.domain.aggregates.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermitPersonDto {
    private Long id;

    private PersonDto person;

    private PermitDto permit;
}

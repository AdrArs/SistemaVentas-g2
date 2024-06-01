package com.codigo.permit_service.domain.aggregates.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermitRequest {
    private String permit;
}

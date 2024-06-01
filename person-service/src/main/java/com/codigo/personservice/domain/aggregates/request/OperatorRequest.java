package com.codigo.personservice.domain.aggregates.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OperatorRequest {
    private String dni;
}

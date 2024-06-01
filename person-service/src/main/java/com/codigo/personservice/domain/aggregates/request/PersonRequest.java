package com.codigo.personservice.domain.aggregates.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PersonRequest {
    private String numDoc;
    private String direcion;
    private String telefono;
    private String correo;
    private String clave;
}

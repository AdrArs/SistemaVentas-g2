package com.codigo.security_service.aggregates.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String correo;
    private String clave;
}

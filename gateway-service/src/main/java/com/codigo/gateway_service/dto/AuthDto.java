package com.codigo.gateway_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    private String jwt;
}

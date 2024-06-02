package com.codigo.venta_service.domain.aggregates.dto;

import lombok.*;

import java.sql.Timestamp;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    private Long idventa;
    private Long idCliente;
    private Long idVendedor;
    private String tipoComprobante;
    private String numComprobante;
    private Timestamp fechaHora;
    private Float igv;
    private Float total;
    private String estado;
}

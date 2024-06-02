package com.codigo.venta_service.domain.aggregates.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {

    private Long idCliente;
    private Long idVendedor;
    private String tipoComprobante;
    private List<DetalleRequest> detalleRequest;
}

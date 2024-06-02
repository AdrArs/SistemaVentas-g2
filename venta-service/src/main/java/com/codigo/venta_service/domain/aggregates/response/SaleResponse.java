package com.codigo.venta_service.domain.aggregates.response;

import com.codigo.venta_service.domain.aggregates.dto.SaleDetailDto;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponse {
    private Long idventa;
    private Long idCliente;
    private Long idVendedor;
    private String tipoComprobante;
    private String numComprobante;
    private Timestamp fechaHora;
    private Float igv;
    private Float total;
    private String estado;
    private List<SaleDetailDto> saleDetailDtoList;
}

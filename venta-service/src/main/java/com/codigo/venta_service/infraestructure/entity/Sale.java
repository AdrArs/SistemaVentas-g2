package com.codigo.venta_service.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "venta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

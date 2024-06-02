package com.codigo.venta_service.infraestructure.dao;

import com.codigo.venta_service.infraestructure.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SaleRepository  extends JpaRepository<Sale,Long> {

    @Query(value = "SELECT generar_numero_boleta()",nativeQuery = true)
    String generateNumBoleta();
    @Query(value = "SELECT generar_numero_factura()",nativeQuery = true)
    String generateNumFactura();
}

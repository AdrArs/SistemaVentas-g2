package com.codigo.venta_service.infraestructure.dao;


import com.codigo.venta_service.infraestructure.entity.Sale;
import com.codigo.venta_service.infraestructure.entity.SaleDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleDetailRepository extends JpaRepository<SaleDetail,Long> {
    List<SaleDetail> findByVenta(Sale sale);
    @Modifying
    @Transactional
    void deleteByVenta(Sale sale);

    @Modifying
    @Transactional
    @Query("DELETE FROM SaleDetail sd WHERE sd.venta.id = :idVenta")
    void deleteByVentaId(Long idVenta);
}

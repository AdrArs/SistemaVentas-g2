package com.codigo.personservice.infraestructure.dao;

import com.codigo.personservice.infraestructure.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier,Long> {
    Optional<Supplier> findByPersonaIdpersona(Long id);
}

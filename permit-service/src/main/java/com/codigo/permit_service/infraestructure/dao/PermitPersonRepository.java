package com.codigo.permit_service.infraestructure.dao;

import com.codigo.permit_service.infraestructure.entity.PermitPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermitPersonRepository extends JpaRepository<PermitPerson,Long> {
    List<PermitPerson> findByPersonaId(Long id);
}

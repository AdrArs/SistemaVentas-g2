package com.codigo.security_service.dao;

import com.codigo.security_service.entity.PermitPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermitPersonRepository extends JpaRepository<PermitPerson,Long> {
    List<PermitPerson> findByPersonaId(Long id);
}

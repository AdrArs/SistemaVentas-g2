package com.codigo.permit_service.infraestructure.dao;

import com.codigo.permit_service.infraestructure.entity.Permit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermitRepository  extends JpaRepository<Permit,Long> {
}

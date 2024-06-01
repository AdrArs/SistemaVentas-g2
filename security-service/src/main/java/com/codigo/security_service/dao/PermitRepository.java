package com.codigo.security_service.dao;

import com.codigo.security_service.entity.Permit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermitRepository  extends JpaRepository<Permit,Long> {
}

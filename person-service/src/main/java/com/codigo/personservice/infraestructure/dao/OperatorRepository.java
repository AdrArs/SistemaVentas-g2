package com.codigo.personservice.infraestructure.dao;

import com.codigo.personservice.infraestructure.entity.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OperatorRepository extends JpaRepository<Operator,Long> {
    Optional<Operator> findByPersonaIdpersona(Long id);
    @Query(value = "SELECT generate_employee_code()",nativeQuery = true)
    String generateCodOperator();
}

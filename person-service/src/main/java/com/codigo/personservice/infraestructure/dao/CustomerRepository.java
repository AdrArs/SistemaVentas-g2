package com.codigo.personservice.infraestructure.dao;

import com.codigo.personservice.infraestructure.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByPersonaIdpersona(Long id);
    @Query(value = "SELECT generate_customer_code()",nativeQuery = true)
    String generateCodCustomer();

}

package com.codigo.personservice.infraestructure.dao;

import com.codigo.personservice.infraestructure.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person,Long> {
    boolean existsByCorreo(String email);
    boolean existsByNumeroDocumento(String numDoc);
    Optional<Person> findByCorreo(String correo);
    Optional<Person> findByNumeroDocumento(String numDoc);
}

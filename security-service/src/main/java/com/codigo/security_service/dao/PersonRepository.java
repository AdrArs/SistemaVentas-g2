package com.codigo.security_service.dao;

import com.codigo.security_service.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByCorreo(String correo);
}

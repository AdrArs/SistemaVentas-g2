package com.codigo.security_service.service;

import com.codigo.security_service.entity.Person;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface PersonService {
    Person findByCorreo(String correo);

    UserDetailsService userDetailService();

}

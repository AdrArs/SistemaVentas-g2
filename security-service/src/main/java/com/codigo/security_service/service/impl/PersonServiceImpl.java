package com.codigo.security_service.service.impl;

import com.codigo.security_service.dao.PermitPersonRepository;
import com.codigo.security_service.dao.PermitRepository;
import com.codigo.security_service.dao.PersonRepository;
import com.codigo.security_service.entity.Permit;
import com.codigo.security_service.entity.PermitPerson;
import com.codigo.security_service.entity.Person;
import com.codigo.security_service.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final PermitRepository permitRepository;
    private final PermitPersonRepository permitPersonRepository;
    @Override
    public Person findByCorreo(String correo) {
        Optional<Person> person = personRepository.findByCorreo(correo);
        return person.orElseGet(Person::new);
    }
    @Override
    public UserDetailsService userDetailService() {
        return username -> {
            Person person = personRepository.findByCorreo(username).orElse(null);
            if ( isNull(person)) {
                throw new UsernameNotFoundException("User not found");
            }

            List<PermitPerson> permitPersons = permitPersonRepository.findByPersonaId(person.getIdpersona());
            List<GrantedAuthority> authorities = permitPersons.stream()
                    .map(permitPerson -> {
                        Permit permit = permitRepository.findById(permitPerson.getPermisoId()).orElse(null);
                        return new SimpleGrantedAuthority(permit.getPermiso());
                    }).collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(person.getCorreo(), person.getClave(), authorities);
        };
    }
}

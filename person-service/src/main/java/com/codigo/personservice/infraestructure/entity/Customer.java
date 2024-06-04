package com.codigo.personservice.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codCliente;
    @ManyToOne
    @JoinColumn(name = "idpersona")
    private Person persona;
}

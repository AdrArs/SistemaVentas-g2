package com.codigo.personservice.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vendedor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Operator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codEmpleado;
    @ManyToOne
    @JoinColumn(name = "idpersona")
    private Person persona;
}

package com.codigo.permit_service.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "persona_permiso")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermitPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long personaId;

    private Long permisoId;

}

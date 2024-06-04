package com.codigo.permit_service.infraestructure.entity;

import com.codigo.permit_service.infraestructure.entity.audit.Audit;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "permiso")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permit extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idpermiso;
    private String permiso;
}

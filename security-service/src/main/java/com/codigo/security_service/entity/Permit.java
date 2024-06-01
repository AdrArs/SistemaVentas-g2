package com.codigo.security_service.entity;

import com.codigo.security_service.entity.audit.Audit;
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
//    @OneToMany(mappedBy = "permiso")
//    private Set<PermitPerson> personas;
}

package com.financiera.servicedesk.rol;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre; // "ADMIN TI", "COORDINADOR TI",  "TECNICO TI", "USUARIO", "GERENCIA TI"

    @Column(length = 200)
    private String descripcion;
}
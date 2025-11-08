package com.financiera.servicedesk.sla;

import com.financiera.servicedesk.categoria.categoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "slas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class sla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private categoria categoria;

    @Column(nullable = false, length = 10)
    private String prioridad; // 'Alta', 'Media', 'Baja'

    @Column(name = "tiempo_max_respuesta", nullable = false)
    private Integer tiempoMaxRespuesta; // En minutos

    @Column(name = "tiempo_max_resolucion", nullable = false)
    private Integer tiempoMaxResolucion; // En minutos
}
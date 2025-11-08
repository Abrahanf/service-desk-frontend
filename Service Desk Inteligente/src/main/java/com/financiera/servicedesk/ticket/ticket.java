package com.financiera.servicedesk.ticket;

import com.financiera.servicedesk.colaborador.colaborador;
import com.financiera.servicedesk.categoria.categoria;
import com.financiera.servicedesk.sla.sla;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_solicitante", nullable = false)
    private colaborador solicitante;

    @ManyToOne
    @JoinColumn(name = "id_agente_asignado")
    private colaborador agenteAsignado;

    @Column(nullable = false, length = 10)
    private String prioridad; // 'Alta', 'Media', 'Baja'

    @Column(nullable = false, length = 20)
    private String estado; // 'Abierto', 'En proceso', 'Resuelto', 'Cerrado'

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 20)
    private String fuente; // 'AI', 'Monitoreo', 'Manual'

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @ManyToOne
    @JoinColumn(name = "id_sla")
    private sla sla;

    @Column(length = 20)
    private String tipo; // 'preticket', 'formal'

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = "Abierto";
        }
        if (tipo == null) {
            tipo = "formal";
        }
    }
}
package com.financiera.servicedesk.comentario;

import com.financiera.servicedesk.ticket.ticket;
import com.financiera.servicedesk.colaborador.colaborador;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_ticket", nullable = false)
    private ticket ticket;

    @ManyToOne
    @JoinColumn(name = "id_autor", nullable = false)
    private colaborador autor;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(columnDefinition = "TEXT")
    private String texto;

    @Column(length = 30)
    private String tipo; // 'actualización', 'nota interna', 'retroalimentación'

    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now();
    }
}
package com.financiera.servicedesk.encuestaSatisfaccion;
import com.financiera.servicedesk.ticket.ticket;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "encuestas_satisfaccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class encuestaSatisfaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_ticket", nullable = false, unique = true)
    private ticket ticket;

    @Column(nullable = false)
    private Integer calificacion; // 1-5

    @Column(columnDefinition = "TEXT")
    private String comentario;
}
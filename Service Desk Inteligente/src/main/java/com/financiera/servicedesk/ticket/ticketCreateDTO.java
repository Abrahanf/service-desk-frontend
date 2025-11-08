package com.financiera.servicedesk.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// DTO para crear ticket
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ticketCreateDTO {
    private Long idCategoria;
    private Long idSolicitante;
    private String prioridad; // 'Alta', 'Media', 'Baja'
    private String descripcion;
    private String fuente; // 'AI', 'Manual'
}

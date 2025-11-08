
package com.financiera.servicedesk.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// DTO para encuesta de satisfacción
@Data
@NoArgsConstructor
@AllArgsConstructor
public class encuestaCreateDTO {
    private Long idTicket;
    private Integer calificacion; // 1-5
    private String comentario;
}
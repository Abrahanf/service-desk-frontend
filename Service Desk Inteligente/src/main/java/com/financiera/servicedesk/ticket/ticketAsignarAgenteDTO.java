
package com.financiera.servicedesk.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// DTO para asignar agente
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ticketAsignarAgenteDTO {
    private Long idAgente;
}

package com.financiera.servicedesk.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// DTO para actualizar estado
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ticketUpdateEstadoDTO {
    private String estado; // 'Abierto', 'En proceso', 'Resuelto', 'Cerrado'
}
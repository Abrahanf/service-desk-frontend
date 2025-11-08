
package com.financiera.servicedesk.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// DTO para agregar comentario
@Data
@NoArgsConstructor
@AllArgsConstructor
public class comentarioCreateDTO {
    private Long idTicket;
    private Long idAutor;
    private String texto;
    private String tipo; // 'actualización', 'nota interna', 'retroalimentación'
}
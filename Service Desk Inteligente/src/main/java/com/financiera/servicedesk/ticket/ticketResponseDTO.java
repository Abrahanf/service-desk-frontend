package com.financiera.servicedesk.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
// DTO para respuesta de ticket
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ticketResponseDTO {
    private Long id;
    private String categoriaResumen;
    private String solicitanteNombre;
    private String agenteAsignadoNombre;
    private String prioridad;
    private String estado;
    private String descripcion;
    private String fuente;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaCierre;
    private String tipo;
}
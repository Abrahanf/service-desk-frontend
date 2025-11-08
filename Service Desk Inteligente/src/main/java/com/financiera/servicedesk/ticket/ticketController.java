package com.financiera.servicedesk.ticket;

import com.financiera.servicedesk.comentario.comentario;
import com.financiera.servicedesk.encuestaSatisfaccion.encuestaSatisfaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:3000")
public class ticketController {

    @Autowired
    private ticketService ticketService;

    // GET: Obtener todos los tickets
    @GetMapping
    public ResponseEntity<List<ticketResponseDTO>> getAllTickets() {
        List<ticketResponseDTO> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    // GET: Obtener tickets de un usuario específico
    @GetMapping("/mis-tickets/{usuarioId}")
    public ResponseEntity<List<ticketResponseDTO>> getMisTickets(@PathVariable Long usuarioId) {
        List<ticketResponseDTO> tickets = ticketService.getTicketsByUsuario(usuarioId);
        return ResponseEntity.ok(tickets);
    }

    // GET: Obtener un ticket por ID
    @GetMapping("/{id}")
    public ResponseEntity<ticket> getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Crear un nuevo ticket
    @PostMapping
    public ResponseEntity<ticket> createTicket(@RequestBody ticketCreateDTO dto) {
        try {
            ticket nuevoTicket = ticketService.createTicket(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTicket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT: Actualizar estado del ticket
    @PutMapping("/{id}/estado")
    public ResponseEntity<ticket> updateEstado(
            @PathVariable Long id,
            @RequestBody ticketUpdateEstadoDTO dto) {
        try {
            ticket ticketActualizado = ticketService.updateEstado(id, dto.getEstado());
            return ResponseEntity.ok(ticketActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT: Asignar agente al ticket
    @PutMapping("/{id}/asignar")
    public ResponseEntity<ticket> asignarAgente(
            @PathVariable Long id,
            @RequestBody ticketAsignarAgenteDTO dto) {
        try {
            ticket ticketActualizado = ticketService.asignarAgente(id, dto.getIdAgente());
            return ResponseEntity.ok(ticketActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET: Obtener comentarios de un ticket
    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<comentario>> getComentarios(@PathVariable Long id) {
        try {
            List<comentario> comentarios = ticketService.getComentariosByTicket(id);
            return ResponseEntity.ok(comentarios);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST: Agregar comentario a un ticket
    @PostMapping("/comentarios")
    public ResponseEntity<comentario> addComentario(@RequestBody comentarioCreateDTO dto) {
        try {
            comentario nuevoComentario = ticketService.addComentario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoComentario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // POST: Crear encuesta de satisfacción
    @PostMapping("/encuesta")
    public ResponseEntity<encuestaSatisfaccion> crearEncuesta(@RequestBody encuestaCreateDTO dto) {
        try {
            encuestaSatisfaccion encuesta = ticketService.crearEncuesta(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(encuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET: Obtener estadísticas de tickets
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getEstadisticas() {
        Map<String, Long> stats = ticketService.getEstadisticas();
        return ResponseEntity.ok(stats);
    }

    // DELETE: Eliminar un ticket (opcional, según tu lógica de negocio)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        // Solo si tu lógica lo permite
        // ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
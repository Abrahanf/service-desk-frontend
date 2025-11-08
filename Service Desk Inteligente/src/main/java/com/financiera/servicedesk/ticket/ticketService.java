package com.financiera.servicedesk.ticket;

import com.financiera.servicedesk.colaborador.colaborador;
import com.financiera.servicedesk.colaborador.colaboradorRepository;
import com.financiera.servicedesk.categoria.categoria;
import com.financiera.servicedesk.categoria.categoriaRepository;
import com.financiera.servicedesk.comentario.comentario;
import com.financiera.servicedesk.comentario.comentarioRepository;
import com.financiera.servicedesk.encuestaSatisfaccion.encuestaSatisfaccion;
import com.financiera.servicedesk.encuestaSatisfaccion.encuestaSatisfaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ticketService {

    @Autowired
    private ticketRepository ticketRepository;

    @Autowired
    private colaboradorRepository colaboradorRepository;

    @Autowired
    private categoriaRepository categoriaRepository;

    @Autowired
    private comentarioRepository comentarioRepository;

    @Autowired
    private encuestaSatisfaccionRepository encuestaRepository;

    // Obtener todos los tickets
    public List<ticketResponseDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener tickets por usuario (solicitante)
    public List<ticketResponseDTO> getTicketsByUsuario(Long usuarioId) {
        return ticketRepository.findBySolicitanteIdOrderByFechaCreacionDesc(usuarioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener un ticket por ID
    public Optional<ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    // Crear un nuevo ticket
    public ticket createTicket(ticketCreateDTO dto) {
        ticket nuevoTicket = new ticket();

        // Buscar categoría
        categoria cat = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        nuevoTicket.setCategoria(cat);

        // Buscar solicitante
        colaborador solicitante = colaboradorRepository.findById(dto.getIdSolicitante())
                .orElseThrow(() -> new RuntimeException("Solicitante no encontrado"));
        nuevoTicket.setSolicitante(solicitante);

        nuevoTicket.setPrioridad(dto.getPrioridad());
        nuevoTicket.setDescripcion(dto.getDescripcion());
        nuevoTicket.setFuente(dto.getFuente());
        nuevoTicket.setEstado("Abierto");
        nuevoTicket.setTipo("formal");

        return ticketRepository.save(nuevoTicket);
    }

    // Actualizar estado del ticket
    public ticket updateEstado(Long ticketId, String nuevoEstado) {
        ticket t = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        t.setEstado(nuevoEstado);

        // Si se cierra, establecer fecha de cierre
        if (nuevoEstado.equals("Cerrado")) {
            t.setFechaCierre(LocalDateTime.now());
        }

        return ticketRepository.save(t);
    }

    // Asignar agente al ticket
    public ticket asignarAgente(Long ticketId, Long agenteId) {
        ticket t = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        colaborador agente = colaboradorRepository.findById(agenteId)
                .orElseThrow(() -> new RuntimeException("Agente no encontrado"));

        t.setAgenteAsignado(agente);
        t.setEstado("En proceso");

        return ticketRepository.save(t);
    }

    // Agregar comentario al ticket
    public comentario addComentario(comentarioCreateDTO dto) {
        comentario c = new comentario();

        ticket t = ticketRepository.findById(dto.getIdTicket())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        c.setTicket(t);

        colaborador autor = colaboradorRepository.findById(dto.getIdAutor())
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
        c.setAutor(autor);

        c.setTexto(dto.getTexto());
        c.setTipo(dto.getTipo());

        return comentarioRepository.save(c);
    }

    // Obtener comentarios de un ticket
    public List<comentario> getComentariosByTicket(Long ticketId) {
        ticket t = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        return comentarioRepository.findByTicketOrderByFechaDesc(t);
    }

    // Crear encuesta de satisfacción
    public encuestaSatisfaccion crearEncuesta(encuestaCreateDTO dto) {
        encuestaSatisfaccion encuesta = new encuestaSatisfaccion();

        ticket t = ticketRepository.findById(dto.getIdTicket())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        encuesta.setTicket(t);

        encuesta.setCalificacion(dto.getCalificacion());
        encuesta.setComentario(dto.getComentario());

        return encuestaRepository.save(encuesta);
    }

    // Obtener estadísticas
    public java.util.Map<String, Long> getEstadisticas() {
        return java.util.Map.of(
                "abiertos", ticketRepository.countByEstado("Abierto"),
                "enProceso", ticketRepository.countByEstado("En proceso"),
                "resueltos", ticketRepository.countByEstado("Resuelto"),
                "cerrados", ticketRepository.countByEstado("Cerrado")
        );
    }

    // Convertir entidad a DTO
    private ticketResponseDTO convertToDTO(ticket t) {
        ticketResponseDTO dto = new ticketResponseDTO();
        dto.setId(t.getId());
        dto.setCategoriaResumen(t.getCategoria().getNombre());
        dto.setSolicitanteNombre(t.getSolicitante().getNombre() + " " + t.getSolicitante().getApellido());
        dto.setAgenteAsignadoNombre(
                t.getAgenteAsignado() != null ?
                        t.getAgenteAsignado().getNombre() + " " + t.getAgenteAsignado().getApellido() :
                        "Sin asignar"
        );
        dto.setPrioridad(t.getPrioridad());
        dto.setEstado(t.getEstado());
        dto.setDescripcion(t.getDescripcion());
        dto.setFuente(t.getFuente());
        dto.setFechaCreacion(t.getFechaCreacion());
        dto.setFechaCierre(t.getFechaCierre());
        dto.setTipo(t.getTipo());
        return dto;
    }
}
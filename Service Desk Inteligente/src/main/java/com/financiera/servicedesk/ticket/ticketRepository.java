package com.financiera.servicedesk.ticket;

import com.financiera.servicedesk.colaborador.colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ticketRepository extends JpaRepository<ticket, Long> {

    // Buscar tickets por solicitante
    List<ticket> findBySolicitante(colaborador solicitante);

    // Buscar tickets por agente asignado
    List<ticket> findByAgenteAsignado(colaborador agente);

    // Buscar por estado
    List<ticket> findByEstado(String estado);

    // Buscar por prioridad
    List<ticket> findByPrioridad(String prioridad);

    // Contar tickets por estado
    @Query("SELECT COUNT(t) FROM ticket t WHERE t.estado = ?1")
    Long countByEstado(String estado);

    // Obtener tickets del usuario ordenados por fecha
    @Query("SELECT t FROM ticket t WHERE t.solicitante.id = ?1 ORDER BY t.fechaCreacion DESC")
    List<ticket> findBySolicitanteIdOrderByFechaCreacionDesc(Long solicitanteId);
}
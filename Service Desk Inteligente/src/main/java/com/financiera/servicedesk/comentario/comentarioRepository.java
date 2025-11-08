package com.financiera.servicedesk.comentario;

import com.financiera.servicedesk.ticket.ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface comentarioRepository extends JpaRepository<comentario, Long> {
    List<comentario> findByTicketOrderByFechaDesc(ticket ticket);
}

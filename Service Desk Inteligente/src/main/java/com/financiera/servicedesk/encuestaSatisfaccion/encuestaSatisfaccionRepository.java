package com.financiera.servicedesk.encuestaSatisfaccion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface encuestaSatisfaccionRepository extends JpaRepository<encuestaSatisfaccion, Long> {
}
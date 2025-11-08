package com.financiera.servicedesk.sla;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface slaRepository extends JpaRepository<sla, Long> {
}
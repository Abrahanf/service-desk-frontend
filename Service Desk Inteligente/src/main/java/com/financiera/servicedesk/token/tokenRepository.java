package com.financiera.servicedesk.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface tokenRepository extends JpaRepository<token, Long> {

    Optional<token> findByToken(String token);

    void deleteByColaboradorId(Long colaboradorId);
}
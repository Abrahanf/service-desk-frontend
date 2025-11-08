package com.financiera.servicedesk.colaborador;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface colaboradorRepository extends JpaRepository<colaborador, Long> {
     Optional<colaborador> findByEmail(String email);

    Optional<colaborador> findByEmailAndContraseña(String email, String contraseña);

    // Métodos personalizados si necesitas
    // Optional<Colaborador> findByEmail(String email);
}
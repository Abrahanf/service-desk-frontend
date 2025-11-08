package com.financiera.servicedesk.token;

import com.financiera.servicedesk.colaborador.colaborador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class tokenService {

    private final tokenRepository tokenRepository;

    @Autowired
    public tokenService(tokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String crearToken(colaborador colaborador) {
        // Generar token único
        String tokenValue = UUID.randomUUID().toString();

        // Crear entidad
        token resetToken = new token();
        resetToken.setToken(tokenValue);
        resetToken.setColaborador(colaborador);
        resetToken.setFechaCreacion(LocalDateTime.now());
        resetToken.setFechaExpiracion(LocalDateTime.now().plusHours(1)); // Expira en 1 hora
        resetToken.setUsado(false);

        // Guardar
        tokenRepository.save(resetToken);

        return tokenValue;
    }

    public token validarToken(String token) {
        token resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        // Validar que no haya expirado
        if (resetToken.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        // Validar que no haya sido usado
        if (resetToken.isUsado()) {
            throw new RuntimeException("Token ya fue utilizado");
        }

        return resetToken;
    }

    public void marcarComoUsado(String token) {
        token resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token no encontrado"));

        resetToken.setUsado(true);
        tokenRepository.save(resetToken);
    }

    public void eliminarTokensAntiguos(Long colaboradorId) {
        tokenRepository.deleteByColaboradorId(colaboradorId);
    }
}
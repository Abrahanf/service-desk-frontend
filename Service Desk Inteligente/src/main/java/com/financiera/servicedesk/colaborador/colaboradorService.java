package com.financiera.servicedesk.colaborador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class colaboradorService {

    @Autowired
    private colaboradorRepository colaboradorRepository;

    // Validar login y clave
    public boolean validarLogin(String email, String contraseña) {
        Optional<colaborador> colaborador = colaboradorRepository.findByEmailAndContraseña(email, contraseña);
        return colaborador.isPresent();
    }

    // Obtener todos los colaboradores
    public List<colaborador> getAllColaboradores() {
        return colaboradorRepository.findAll();
    }

    // Obtener colaborador por ID
    public Optional<colaborador> getColaboradorById(Long id) {
        return colaboradorRepository.findById(id);
    }

    // Crear un nuevo colaborador
    public colaborador createColaborador(colaborador colaborador) {
        // Aquí puedes agregar validaciones antes de guardar
        return colaboradorRepository.save(colaborador);
    }

    // Actualizar un colaborador
    public colaborador updateColaborador(Long id, colaborador colaboradorActualizado) {
        return colaboradorRepository.findById(id)
                .map(colaborador -> {
                    colaborador.setNombre(colaboradorActualizado.getNombre());
                    colaborador.setApellido(colaboradorActualizado.getApellido());
                    colaborador.setArea(colaboradorActualizado.getArea());
                    colaborador.setCargo(colaboradorActualizado.getCargo());
                    colaborador.setEmail(colaboradorActualizado.getEmail());
                    colaborador.setTelefono(colaboradorActualizado.getTelefono());
                    colaborador.setContraseña(colaboradorActualizado.getContraseña());
                    colaborador.setRol(colaboradorActualizado.getRol());
                    return colaboradorRepository.save(colaborador);
                })
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado con id: " + id));
    }

    // Eliminar un colaborador
    public void deleteColaborador(Long id) {
        colaboradorRepository.deleteById(id);
    }

    public void actualizarContraseña(colaborador colaborador, String nuevaClave) {
        colaborador.setContraseña(nuevaClave);
        colaboradorRepository.save(colaborador);
    }

}
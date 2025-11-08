package com.financiera.servicedesk.colaborador;

import com.financiera.servicedesk.security.JwtUtil;
import com.financiera.servicedesk.token.forgotPasswordDTO;
import com.financiera.servicedesk.token.resetPasswordDTO;
import com.financiera.servicedesk.token.token;
import com.financiera.servicedesk.token.tokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/colaboradores")
@CrossOrigin(origins = "http://localhost:3000")
public class colaboradorController {

    @Autowired
    private colaboradorService colaboradorService;
    @Autowired
    private tokenService tokenService;
    @Autowired
    private colaboradorRepository colaboradorRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody colaboradorLoginDTO colaboradorLoginDTO) {
        try {
            boolean valido = colaboradorService.validarLogin(
                    colaboradorLoginDTO.getEmail(),
                    colaboradorLoginDTO.getContraseña()
            );

            if (valido) {
                // Buscar el colaborador por email
                Optional<colaborador> colaboradorOpt = colaboradorRepository.findByEmail(
                        colaboradorLoginDTO.getEmail()
                );

                if (colaboradorOpt.isPresent()) {
                    colaborador col = colaboradorOpt.get();
                    String jwt = jwtUtil.generarToken(col.getEmail());

                    // Crear respuesta con datos del colaborador
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", jwt);
                    response.put("colaborador", col);

                    // Crear objeto colaborador para la respuesta
                    Map<String, Object> colaboradorData = new HashMap<>();
                    colaboradorData.put("id", col.getId());
                    colaboradorData.put("nombre", col.getNombre());
                    colaboradorData.put("apellido", col.getApellido());
                    colaboradorData.put("email", col.getEmail());
                    colaboradorData.put("area", col.getArea());
                    colaboradorData.put("cargo", col.getCargo());
                    colaboradorData.put("telefono", col.getTelefono());

                    // Agregar rol
                    if (col.getRol() != null) {
                        Map<String, Object> rolData = new HashMap<>();
                        rolData.put("id", col.getRol().getId());
                        rolData.put("nombre", col.getRol().getNombre());
                        rolData.put("descripcion", col.getRol().getDescripcion());
                        colaboradorData.put("rol", rolData);
                    }

                    response.put("colaborador", colaboradorData);

                    return ResponseEntity.ok(response);
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Credenciales incorrectas"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en el servidor"));
        }
    }

    // GET: Obtener todos los colaboradores
    @GetMapping
    public ResponseEntity<List<colaborador>> getAllColaboradores() {
        List<colaborador> colaboradores = colaboradorService.getAllColaboradores();
        return ResponseEntity.ok(colaboradores);
    }

    // GET: Obtener un colaborador por ID
    @GetMapping("/{id}")
    public ResponseEntity<colaborador> getColaboradorById(@PathVariable Long id) {
        return colaboradorService.getColaboradorById(id)
                .map(ResponseEntity::ok)                        // Si existe: 200 OK
                .orElse(ResponseEntity.notFound().build());     // Si no existe: 404 NOT FOUND
    }

    // POST: Crear un nuevo colaborador
    @PostMapping
    public ResponseEntity<colaborador> createColaborador(@RequestBody colaborador colaborador) {
        colaborador nuevoColaborador = colaboradorService.createColaborador(colaborador);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoColaborador);
    }

    // PUT: Actualizar un colaborador
    @PutMapping("/{id}")
    public ResponseEntity<colaborador> updateColaborador(
            @PathVariable Long id,
            @RequestBody colaborador colaborador) {
        try {
            colaborador colaboradorActualizado = colaboradorService.updateColaborador(id, colaborador);
            return ResponseEntity.ok(colaboradorActualizado); // 200 OK si funciona
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();         // 404 si no encuentra
        }
    }

    // DELETE: Eliminar un colaborador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColaborador(@PathVariable Long id) {
        colaboradorService.deleteColaborador(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody forgotPasswordDTO dto) {
        try {
            // 1. Buscar al colaborador por email
            Optional<colaborador> colaboradorOpt = colaboradorRepository.findByEmail(dto.getEmail());

            if (colaboradorOpt.isEmpty()) {
                return ResponseEntity.ok(Map.of("mensaje", "Si el email existe, recibirás un correo"));
            }

            colaborador colaborador = colaboradorOpt.get();

            // 2. Eliminar tokens antiguos de este usuario (si existen)
            tokenService.eliminarTokensAntiguos(colaborador.getId());

            // 3. Crear nuevo token
            String token = tokenService.crearToken(colaborador);

            // 4. TODO: Enviar email con el token (por ahora solo lo mostramos)
            // emailService.enviarEmailRecuperacion(colaborador.getEmail(), token);

            // Por ahora, para testing, devolvemos el token (NUNCA hacer esto en producción)
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Si el email existe, recibirás un correo",
                    "token", token  // ⚠️ SOLO PARA TESTING - Quitar en producción
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
                   // .body(Map.of("error", "Error al procesar la solicitud"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody resetPasswordDTO dto) {
        try {
            // 1. Validar que las contraseñas coincidan
            if (!dto.getNuevaClave().equals(dto.getNuevaClaveConfirmacion())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Las contraseñas no coinciden"));
            }

            // 2. Validar que la contraseña cumpla requisitos mínimos
            if (dto.getNuevaClave().length() < 6) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "La contraseña debe tener al menos 6 caracteres"));
            }

            // 3. Validar el token
            token resetToken = tokenService.validarToken(dto.getToken());

            // 4. Obtener el colaborador asociado al token
            colaborador colaborador = resetToken.getColaborador();

            // 5. Actualizar la contraseña
            colaborador.setContraseña(dto.getNuevaClave());
            colaboradorRepository.save(colaborador);

            // 6. Marcar el token como usado
            tokenService.marcarComoUsado(dto.getToken());

            return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada exitosamente"));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar la solicitud"));
        }
        }
}
package com.hotelandinoplaza.sistemagestion.controller;

import com.hotelandinoplaza.sistemagestion.entity.Usuario;
import com.hotelandinoplaza.sistemagestion.repository.UsuarioRepository;
import com.hotelandinoplaza.sistemagestion.seguridad.UsuarioPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public ResponseEntity<?> getUsuarioLogueado(@AuthenticationPrincipal UsuarioPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        return ResponseEntity.ok(Map.of(
                "nombre", principal.getNombreCompleto(),
                "email", principal.getUsername(),
                "rol", principal.getRol(),
                // 🟢 Asegúrate de agregar estas dos líneas en el retorno del mapa en Java:
                "tipoDocumento", principal.getUsuario().getTipoDocumento(),
                "numeroDocumento", principal.getUsuario().getNumeroDocumento()
        ));
    }
    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario nuevoUsuario) {
        // Validamos si el correo ya existe
        if (usuarioRepository.findByEmail(nuevoUsuario.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body(Map.of("error", "El correo electrónico ya está registrado."));
        }

        // 🛡️ VALIDACIÓN PREVENTIVA DE DOCUMENTOS
        if (nuevoUsuario.getTipoDocumento() == null || nuevoUsuario.getNumeroDocumento() == null) {
            return ResponseEntity.status(400).body(Map.of("error", "El tipo y número de documento son obligatorios."));
        }

        // 🔐 Encriptación de clave
        nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));

        // Forzamos el rol "USER" para registros públicos
        nuevoUsuario.setRol("USER");

        usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado exitosamente con documento enlazado."));
    }
}
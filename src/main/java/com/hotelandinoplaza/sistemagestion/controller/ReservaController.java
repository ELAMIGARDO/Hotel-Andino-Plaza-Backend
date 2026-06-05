package com.hotelandinoplaza.sistemagestion.controller;

import com.hotelandinoplaza.sistemagestion.entity.Reserva;
import com.hotelandinoplaza.sistemagestion.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final SimpMessagingTemplate messagingTemplate;

    // 1. Obtener todas las reservas
    @GetMapping
    public ResponseEntity<List<Reserva>> obtenerTodas() {
        List<Reserva> reservas = reservaService.listarTodas();
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    // 2. Buscar una reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        Reserva reserva = buscarPorId(id);
        return new ResponseEntity<>(reserva, HttpStatus.OK);
    }

    private Reserva buscarPorId(Long id) {
        return reservaService.buscarPorId(id);
    }

    // 3. Crear una nueva reserva
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Reserva reserva) {
        if (reserva.getEstado() == null || reserva.getEstado().isEmpty()) {
            reserva.setEstado("ACTIVA");
        }

        boolean ocupada = reservaService.verificarCruce(
                reserva.getHabitacion().getId(),
                reserva.getFechaIngreso(),
                reserva.getFechaSalida()
        );

        if (ocupada) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La habitación no está disponible en esas fechas.");
        }

        Reserva nuevaReserva = reservaService.guardar(reserva);
        messagingTemplate.convertAndSend("/topic/disponibilidad", "UPDATE_RESERVA");
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    // 4. Finalizar / Liberar habitación
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<String> finalizar(@PathVariable Long id) {
        try {
            reservaService.finalizarReserva(id);
            messagingTemplate.convertAndSend("/topic/disponibilidad", "UPDATE_RESERVA");
            return ResponseEntity.ok().body("Habitación liberada con éxito y reserva archivada.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Eliminar una reserva por completo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        reservaService.eliminar(id);
        messagingTemplate.convertAndSend("/topic/disponibilidad", "UPDATE_RESERVA");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
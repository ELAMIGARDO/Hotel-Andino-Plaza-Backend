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
@RequiredArgsConstructor // 💡 Lombok creará el constructor automático para TODOS los campos "final"
public class ReservaController {

    private final ReservaService reservaService; // 🚀 Inyectado correctamente por Lombok gracias a 'final'
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
        Reserva reserva = reservaService.buscarPorId(id);
        return new ResponseEntity<>(reserva, HttpStatus.OK);
    }

    // 3. Crear una nueva reserva (CORREGIDO PARA INTERCEPTAR ERRORES Y ADMITIR MANTENIMIENTO)
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Reserva reserva) {
        // Si desde React no mandan estado, le ponemos ACTIVA por defecto
        if (reserva.getEstado() == null || reserva.getEstado().isEmpty()) {
            reserva.setEstado("ACTIVA");
        }

        try {
            // 🚀 Llama al método del Service que procesa de forma segura el mantenimiento y clientes
            Reserva nuevaReserva = reservaService.guardar(reserva);

            // 🔥 TIEMPO REAL: Notificamos a React para actualizar la vista inmediatamente
            messagingTemplate.convertAndSend("/topic/disponibilidad", "UPDATE_RESERVA");

            return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            // Envía a tu Frontend de React el texto exacto del error con código 409 (Conflict)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // 4. Finalizar / Liberar habitación
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<String> finalizar(@PathVariable Long id) {
        try {
            reservaService.finalizarReserva(id);
            // 🔥 TIEMPO REAL: Notificamos a React para que pinte la celda de color verde inmediatamente
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
        // 🔥 TIEMPO REAL: Avisamos que se eliminó del registro para limpiar la línea de tiempo
        messagingTemplate.convertAndSend("/topic/disponibilidad", "UPDATE_RESERVA");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 📊 6. ENDPOINT DASHBOARD: Expone las ganancias reales para consumirlas desde React
    @GetMapping("/ganancias")
    public ResponseEntity<Double> obtenerGananciasReales() {
        Double total = reservaService.obtenerTotalIngresos();
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelar(@PathVariable Long id, @RequestBody String motivo) {
        try {
            // Ejecuta la lógica del service
            reservaService.cancelarReserva(id, motivo);

            // 🔥 TIEMPO REAL: Avisamos a React para que pinte la tabla de cancelaciones al instante
            messagingTemplate.convertAndSend("/topic/disponibilidad", "UPDATE_RESERVA");

            return ResponseEntity.ok().body("Reserva cancelada con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
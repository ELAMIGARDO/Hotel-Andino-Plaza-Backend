package com.hotelandinoplaza.sistemagestion.service;

import com.hotelandinoplaza.sistemagestion.entity.Reserva;
import com.hotelandinoplaza.sistemagestion.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    // 1. Listar todas las reservas
    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    // 🚀 MÉTODO CORREGIDO: Centraliza la lógica inteligente para mantenimientos y clientes
    public Reserva guardar(Reserva reserva) {
        // A. Identificar si es un bloqueo interno (Mantenimiento o Limpieza)
        boolean esBloqueoInterno = "00000000".equals(reserva.getNumeroDocumento());

        // B. Regla de seguridad: Evitar que un cliente real reserve 0 noches
        if (reserva.getFechaIngreso().isEqual(reserva.getFechaSalida()) && !esBloqueoInterno) {
            throw new IllegalArgumentException("Error: Un cliente real debe hospedarse al menos 1 noche.");
        }

        // C. Validar que la fecha de salida no sea anterior a la de ingreso
        if (reserva.getFechaSalida().isBefore(reserva.getFechaIngreso())) {
            throw new IllegalArgumentException("Error: La fecha de salida no puede ser anterior a la de ingreso.");
        }

        // D. Verificar cruces de fechas reales usando el repositorio corregido con (< y >)
        boolean habitacionOcupada = reservaRepository.existeCruceDeFechas(
                reserva.getHabitacion().getId(),
                reserva.getFechaIngreso(),
                reserva.getFechaSalida()
        );

        if (habitacionOcupada) {
            throw new IllegalArgumentException("La habitación no está disponible en esas fechas.");
        }

        // E. Guardar de forma segura en la base de datos
        return reservaRepository.save(reserva);
    }

    // 3. Buscar una reserva por su ID
    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La reserva con ID " + id + " no existe"));
    }

    // 4. Finalizar o liberar una habitación
    public void finalizarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La reserva con ID " + id + " no existe"));
        reserva.setEstado("FINALIZADA");
        reservaRepository.save(reserva);
    }

    // 5. Eliminar una reserva por completo
    public void eliminar(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se puede eliminar, La reserva con ID " + id + " no existe"
            );
        }
        reservaRepository.deleteById(id);
    }

    // 6. Verificar cruces directos (mantenido por compatibilidad si lo necesitas en otra clase)
    public boolean verificarCruce(Long habitacionId, LocalDate fechaIngreso, LocalDate fechaSalida) {
        return reservaRepository.existeCruceDeFechas(habitacionId, fechaIngreso, fechaSalida);
    }

    // 📊 MÉTODO NUEVO: Suma las ganancias reales excluyendo los bloqueos internos
    public Double obtenerTotalIngresos() {
        return reservaRepository.calcularTotalIngresosDashboard();
    }

    public void cancelarReserva(Long id, String motivo) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La reserva con ID " + id + " no existe"));

        // 💡 Blindaje en Spring Boot: Contar palabras de la cadena recibida
        String motivoLimpio = motivo != null ? motivo.trim() : "";
        long conteoPalabras = motivoLimpio.isEmpty() ? 0 : motivoLimpio.split("\\s+").length;

        if (conteoPalabras > 10) {
            throw new IllegalArgumentException("Error: El motivo de cancelación no puede exceder las 10 palabras.");
        }

        reserva.setEstado("CANCELADA");
        reserva.setMotivoCancelacion(motivoLimpio);
        reservaRepository.save(reserva);
    }

}
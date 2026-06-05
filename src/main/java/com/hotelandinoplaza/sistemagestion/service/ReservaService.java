package com.hotelandinoplaza.sistemagestion.service;

import com.hotelandinoplaza.sistemagestion.entity.Reserva;
import com.hotelandinoplaza.sistemagestion.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public Reserva guardar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La reserva con ID " + id + " no existe"));
    }

    public void finalizarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La reserva con ID " + id + " no existe"));
        reserva.setEstado("FINALIZADA");
        reservaRepository.save(reserva);
    }

    public void eliminar(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se puede eliminar, la reserva con ID " + id + " no existe"
            );
        }
        reservaRepository.deleteById(id);
    }

    public boolean verificarCruce(Long habitacionId, LocalDate fechaIngreso, LocalDate fechaSalida) {
        return reservaRepository.existeCruceDeFechas(habitacionId, fechaIngreso, fechaSalida);
    }
}
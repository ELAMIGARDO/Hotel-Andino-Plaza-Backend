package com.hotelandinoplaza.sistemagestion.repository;

import com.hotelandinoplaza.sistemagestion.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // 📊 1. SOLUCIÓN DASHBOARD: Suma solo clientes reales (ignora los bloqueos internos)
    @Query("SELECT COALESCE(SUM(r.habitacion.precio), 0) FROM Reserva r " +
            "WHERE r.estado = 'FINALIZADA' " +
            "AND r.numeroDocumento != '00000000'")
    Double calcularTotalIngresosDashboard();

    // 🔍 2. SOLUCIÓN FECHAS: Verifica cruces usando lógica por noches de hotel (< y >)
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.habitacion.id = :habitacionId " +
            "AND r.estado NOT IN ('FINALIZADA', 'CANCELADA') " +
            "AND (:fechaIngreso < r.fechaSalida AND :fechaSalida > r.fechaIngreso)")
    boolean existeCruceDeFechas(
            @Param("habitacionId") Long habitacionId,
            @Param("fechaIngreso") LocalDate fechaIngreso,
            @Param("fechaSalida") LocalDate fechaSalida
    );
}
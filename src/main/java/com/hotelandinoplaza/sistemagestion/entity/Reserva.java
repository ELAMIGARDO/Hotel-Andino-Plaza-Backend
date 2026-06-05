package com.hotelandinoplaza.sistemagestion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombreCliente;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
    private String estado;
    private String motivoCancelacion;
    @ManyToOne
    private Habitacion habitacion;
}

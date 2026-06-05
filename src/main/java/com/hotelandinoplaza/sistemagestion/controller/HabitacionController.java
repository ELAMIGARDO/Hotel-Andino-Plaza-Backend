package com.hotelandinoplaza.sistemagestion.controller;

import com.hotelandinoplaza.sistemagestion.entity.Habitacion;
import com.hotelandinoplaza.sistemagestion.service.HabitacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HabitacionController {
    private final HabitacionService habitacionService;

    // 1. Obtener todas las habitaciones del hotel
    @GetMapping
    public ResponseEntity<List<Habitacion>> obtenerTodas() {
        List<Habitacion> habitaciones = habitacionService.listarTodas();
        return new ResponseEntity<>(habitaciones, HttpStatus.OK);
    }

    // 2. Buscar una habitación específica por su ID
    @GetMapping("{id}")
    public ResponseEntity<Habitacion> obtenerPorId(
    @PathVariable
    Long id){
        Habitacion habitacion = habitacionService.buscarPorId(id);
        return new ResponseEntity<>(habitacion, HttpStatus.OK);
    }

    // 3. Crear una nueva habitación
    @PostMapping
    public ResponseEntity<Habitacion> crear(@RequestBody Habitacion habitacion){
        Habitacion nuevaHabitacion = habitacionService.guardar(habitacion);
        return new ResponseEntity<>(nuevaHabitacion , HttpStatus.OK);
    }

    // 4. Eliminar una habitación por ID
    @DeleteMapping
    public ResponseEntity<Habitacion>eliminar(@PathVariable Long id){
        Habitacion habitacion = habitacionService.buscarPorId(id);
        if(habitacion == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        habitacionService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

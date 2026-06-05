package com.hotelandinoplaza.sistemagestion.service;

import com.hotelandinoplaza.sistemagestion.entity.Habitacion;
import com.hotelandinoplaza.sistemagestion.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;
import java.util.List;


@RequiredArgsConstructor
@Service
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;

    public List<Habitacion> listarTodas(){
        return habitacionRepository.findAll();
    }

    public Habitacion guardar(Habitacion habitacion){
        return habitacionRepository.save(habitacion);
    }

    public Habitacion buscarPorId(Long id){
        return habitacionRepository.findById(id).orElseThrow(()-> new ResolutionException("La habitación con ID " + id + " no existe."));
    }

    public void eliminar(Long id){
        if(!habitacionRepository.existsById(id)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,"No se puede eliminar, La habitacion con ID" + id + "no existe");
        }
        habitacionRepository.deleteById(id);
    }
}

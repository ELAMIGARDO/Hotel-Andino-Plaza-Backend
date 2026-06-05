package com.hotelandinoplaza.sistemagestion.repository;

import com.hotelandinoplaza.sistemagestion.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface HabitacionRepository  extends JpaRepository<Habitacion, Long> {
}

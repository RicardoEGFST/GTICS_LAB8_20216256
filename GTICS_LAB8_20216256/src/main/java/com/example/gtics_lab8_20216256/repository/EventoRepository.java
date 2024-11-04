package com.example.gtics_lab8_20216256.repository;

import com.example.gtics_lab8_20216256.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {


    @Query("SELECT e FROM Evento e WHERE e.fecha >= :fecha ORDER BY e.fecha")
    List<Evento> EncontrarFechaFiltrada(LocalDate fecha);
}

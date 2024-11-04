package com.example.gtics_lab8_20216256.controller;

import com.example.gtics_lab8_20216256.entity.Evento;
import com.example.gtics_lab8_20216256.entity.Reserva;
import com.example.gtics_lab8_20216256.repository.EventoRepository;
import com.example.gtics_lab8_20216256.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EventoRepository eventoRepository;


    @PostMapping("/reservar")
    public ResponseEntity<HashMap<String, Object>> reservarCupos(@RequestBody Reserva reserva) {
        HashMap<String, Object> response = new HashMap<>();


        Optional<Evento> optionalEvento = eventoRepository.findById(reserva.getEvento().getId());


        if (!optionalEvento.isPresent()) {
            response.put("result", "error");
            response.put("msg", "Evento no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Evento evento = optionalEvento.get();


        if (evento.getReservasAct() + reserva.getCupos() > evento.getCapacidadMax()) {
            response.put("result", "error");
            response.put("msg", "No hay cupos suficientes disponibles.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }


        evento.setReservasAct(evento.getReservasAct() + reserva.getCupos());
        eventoRepository.save(evento);


        reserva.setNombre(reserva.getNombre());
        reserva.setCorreo(reserva.getCorreo());
        reserva.setCupos(reserva.getCupos());
        reserva.setEvento(evento);


        Reserva nuevaReserva = reservaRepository.save(reserva);


        response.put("estado", "Reserva creada!");
        response.put("reservaId", nuevaReserva.getId());
        response.put("nombre", nuevaReserva.getNombre());
        response.put("correo", nuevaReserva.getCorreo());
        response.put("cupos", nuevaReserva.getCupos());
        response.put("eventoId", evento.getId());
        response.put("result", "ok");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<HashMap<String, Object>> cancelarReserva(@PathVariable("id") int reservaId) {
        HashMap<String, Object> response = new HashMap<>();


        Optional<Reserva> optionalReserva = reservaRepository.findById(reservaId);
        if (!optionalReserva.isPresent()) {
            response.put("result", "error");
            response.put("msg", "Reserva no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Reserva reserva = optionalReserva.get();
        Evento evento = reserva.getEvento();


        evento.setReservasAct(evento.getReservasAct() - reserva.getCupos());
        eventoRepository.save(evento);
        reservaRepository.delete(reserva);


        response.put("estado", "Reserva cancelada!");
        response.put("reservaId", reservaId);
        response.put("eventoId", evento.getId());
        response.put("cuposLiberados", reserva.getCupos());
        response.put("result", "ok");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

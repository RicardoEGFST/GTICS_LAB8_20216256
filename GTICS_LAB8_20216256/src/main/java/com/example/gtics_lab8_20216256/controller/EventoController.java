package com.example.gtics_lab8_20216256.controller;

import com.example.gtics_lab8_20216256.entity.Evento;
import com.example.gtics_lab8_20216256.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/Eventos")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;


    @GetMapping("/Listar")
    public List<Evento> listarEventos(
            @RequestParam(value = "fecha", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (fecha != null) {
            //Se supone que si se ingresa una fecha se filtra por esta fecha
            return eventoRepository.EncontrarFechaFiltrada(fecha);
        } else {
            //Si no hay fecha entonces se supone que no se aplicó el filtro y por tanto devuelve todo
            return eventoRepository.findAll();
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<HashMap<String, Object>> crearEvento(@RequestBody Evento evento,
                                                               @RequestParam(value = "Id", required = false) boolean Id) {
        HashMap<String, Object> response = new HashMap<>();

        // Validación de la fecha futura
        if (evento.getFecha().isBefore(LocalDate.now())) {
            response.put("result", "error");
            response.put("estado", "Error, debe ingresar una fecha válida.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (Id) {
            response.put("id", evento.getId());
        }
        response.put("estado", "Se creó el evento");
        response.put("result", "ok");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}


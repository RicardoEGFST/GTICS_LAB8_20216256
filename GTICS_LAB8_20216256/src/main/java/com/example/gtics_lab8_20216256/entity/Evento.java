package com.example.gtics_lab8_20216256.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="evento")

public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEvento", nullable = false)
    private Integer id;

    @Column(name = "nombreEvento", length = 45)
    private String nombre;

    @Column(name = "fechaEvento")
    private LocalDate fecha;

    @Column(name = "categoria", length = 45)
    private String categoria;

    @Column(name="capacidadMax")
    private Integer capacidadMax;

    @Column(name="reservasAct")
    private int reservasAct;
}

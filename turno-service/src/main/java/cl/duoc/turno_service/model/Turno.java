package cl.duoc.turno_service.model;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "turno")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String turno;

    @Column(name = "hora_entrada",nullable = false)
    private LocalTime horaEntrada;

    @Column(name = "hora_salida",nullable = false)
    private LocalTime horaSalida;

    @Column(name = "id_bibliotecario",nullable = false)
    private Long idBibliotecario;
}

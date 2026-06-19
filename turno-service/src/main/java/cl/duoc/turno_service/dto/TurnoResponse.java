package cl.duoc.turno_service.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TurnoResponse {
    private Long id;
    private String turno;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private BibliotecarioResponse bibliotecario;
}
package cl.duoc.turno_service.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TurnoRequest {
    @NotBlank(message = "El turno no puede estar vacío.")
    @Size(max = 40, message = "El largo máximo es de 40 caracteres.")
    private String turno;

    @NotNull(message = "La hora de entrada debe ser en formato HH:mm:ss.SSS")
    private LocalTime horaEntrada;

    @NotNull(message = "La hora de salida debe ser en formato HH:mm:ss.SSS")
    private LocalTime horaSalida;

    @NotNull(message = "El ID del bibliotecario no puede estar vacío.")
    private Long idBibliotecario;
}
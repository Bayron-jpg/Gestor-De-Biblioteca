package cl.duoc.autor_service.dto;

import java.time.LocalDate;

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
public class AutorRequest {
    @NotBlank(message = "El nombre del autor no puede estar vacío.")
    @Size(max = 60, message = "El largo máximo es de 60 caracteres.")
    private String nombre;
    
    @NotNull(message = "La fecha debe ser formato 'YYYY-MM-DD'.")
    private LocalDate fechaNacimiento;
}
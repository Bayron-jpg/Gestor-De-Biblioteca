package cl.duoc.libro_service.dto;

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
public class LibroRequest {
    @NotBlank(message = "El título no puede estar vacío.")
    @Size(max = 60, message = "El largo máximo es de 60 caracteres.")
    private String titulo;
    
    @NotBlank(message = "El isbn no puede estar vacío.")
    @Size(max = 50, message = "El largo máximo es de 50 caracteres.")
    private String isbn;

    @NotNull(message = "La fecha debe ser formato 'YYYY-MM-DD'.")
    private LocalDate fechaPublicacion;

    @NotNull(message = "El ID del Autor no puede estar vacío.")
    private Long idAutor;

    @NotNull(message = "El ID del Género no puede estar vacío.")
    private Long idGenero;
}
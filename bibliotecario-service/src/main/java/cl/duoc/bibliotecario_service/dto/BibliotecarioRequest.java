package cl.duoc.bibliotecario_service.dto;

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
public class BibliotecarioRequest {
    @NotBlank(message = "El nombre del bibliotecario no puede estar vacío.")
    @Size(max = 50, message = "El largo máximo es de 50 caracteres.")
    private String nombre;

    @NotBlank(message = "El apellido del bibliotecario no puede estar vacío.")
    @Size(max = 50, message = "El largo máximo es de 50 caracteres.")
    private String apellido;

    @NotNull(message = "La edad del bibliotecario no puede estar vacía.")
    private Integer edad;
}
package cl.duoc.usuario_service.dto;

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
public class UsuarioRequest {

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 40, message = "El largo máximo es de 40 caracteres.")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío.")
    @Size(max = 40, message = "El largo máximo es de 40 caracteres.")
    private String apellido;

    @NotBlank(message = "El gmail no puede estar vacío.")
    @Size(max = 60, message = "El largo máximo es de 60 caracteres.")
    private String gmail;

    @NotNull(message = "El ID del teléfono no puede estar vacío.")
    private Long idTelefono;

    @NotNull(message = "El ID de la dirección no puede estar vacío.")
    private Long idDireccion;
}
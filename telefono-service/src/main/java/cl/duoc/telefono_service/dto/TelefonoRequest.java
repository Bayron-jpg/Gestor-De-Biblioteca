package cl.duoc.telefono_service.dto;

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
public class TelefonoRequest {
    @NotNull(message = "El número no puede estar vacío.")
    private Integer numero;

    @NotBlank(message = "El tipo de número no puede estar vacío.")
    @Size(max = 20, message = "El largo máximo es de 20 caracteres.")
    private String tipo;
}

package cl.duoc.direccion_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DireccionRequest {
    @NotBlank(message = "La dirección no puede estar vacía.")
    @Size(max = 20, message = "El largo máximo es de 20 caracteres.")
    private String direccion;

    @NotBlank(message = "La comuna no puede estar vacía.")
    @Size(max = 20, message = "El largo máximo es de 20 caracteres.")
    private String comuna;

    @NotBlank(message = "La región no puede estar vacía.")
    @Size(max = 20, message = "El largo máximo es de 20 caracteres.")
    private String region;
}

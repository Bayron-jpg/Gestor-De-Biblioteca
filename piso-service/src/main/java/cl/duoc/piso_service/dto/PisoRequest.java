package cl.duoc.piso_service.dto;

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
public class PisoRequest {
    @NotBlank(message = "El número del piso no puede estar vacío.")
    @Size(max = 3, message = "El largo máximo es de 3 caracteres.")
    private String numero;
    
    @NotBlank(message = "La descripción del piso no puede estar vacía.")
    @Size(max = 255, message = "El largo máximo es de 255 caracteres.")
    private String descripcion;
}
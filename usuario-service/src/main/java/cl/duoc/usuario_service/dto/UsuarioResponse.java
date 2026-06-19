package cl.duoc.usuario_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String gmail;
    private TelefonoResponse telefono;
    private DireccionResponse direccion;
}
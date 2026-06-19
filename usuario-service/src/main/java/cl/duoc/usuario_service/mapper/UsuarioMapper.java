package cl.duoc.usuario_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.usuario_service.dto.DireccionResponse;
import cl.duoc.usuario_service.dto.TelefonoResponse;
import cl.duoc.usuario_service.dto.UsuarioRequest;
import cl.duoc.usuario_service.dto.UsuarioResponse;
import cl.duoc.usuario_service.model.Usuario;

@Component
public class UsuarioMapper {
    // Transformar de JSON a Entidad MySQL
    public Usuario toEntity (UsuarioRequest request){
        return Usuario.builder()
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .gmail(request.getGmail())
            .idTelefono(request.getIdTelefono())
            .idDireccion(request.getIdDireccion())
            .build();
    }

    // Transformar de Entidad MySQL a JSON
    public UsuarioResponse toResponse(Usuario usuario, TelefonoResponse telResponse, DireccionResponse dirResponse){
        return UsuarioResponse.builder()
            .id(usuario.getId())
            .nombre(usuario.getNombre())
            .apellido(usuario.getApellido())
            .gmail(usuario.getGmail())
            .telefono(telResponse)
            .direccion(dirResponse)
            .build();
    }
}
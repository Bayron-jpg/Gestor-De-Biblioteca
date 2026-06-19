package cl.duoc.bibliotecario_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.bibliotecario_service.dto.BibliotecarioRequest;
import cl.duoc.bibliotecario_service.dto.BibliotecarioResponse;
import cl.duoc.bibliotecario_service.model.Bibliotecario;

@Component
public class BibliotecarioMapper {
    // Transformar de JSON a Entidad MySQL
    public Bibliotecario toEntity (BibliotecarioRequest request){
        return Bibliotecario.builder()
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .edad(request.getEdad())
            .build();
    }

    // Transformar de Entidad MySQL a JSON
    public BibliotecarioResponse toResponse(Bibliotecario bibliotecario){
        return BibliotecarioResponse.builder()
            .id(bibliotecario.getId())
            .nombre(bibliotecario.getNombre())
            .apellido(bibliotecario.getApellido())
            .edad(bibliotecario.getEdad())
            .build();
    }
}
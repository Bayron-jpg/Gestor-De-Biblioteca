package cl.duoc.telefono_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.telefono_service.dto.TelefonoRequest;
import cl.duoc.telefono_service.dto.TelefonoResponse;
import cl.duoc.telefono_service.model.Telefono;

@Component
public class TelefonoMapper {
    // Transformar de JSON a Entidad MySQL
    public Telefono toEntity (TelefonoRequest request){
        return Telefono.builder()
            .numero(request.getNumero())
            .tipo(request.getTipo())
            .build();
    }

    // Transformar de Entidad MySQL a JSON
    public TelefonoResponse toResponse(Telefono telefono){
        return TelefonoResponse.builder()
            .id(telefono.getId())
            .numero(telefono.getNumero())
            .tipo(telefono.getTipo())
            .build();
    }
}

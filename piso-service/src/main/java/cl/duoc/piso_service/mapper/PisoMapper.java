package cl.duoc.piso_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.piso_service.dto.PisoRequest;
import cl.duoc.piso_service.dto.PisoResponse;
import cl.duoc.piso_service.model.Piso;

@Component
public class PisoMapper {
    // Transformar de JSON a Entidad MySQL
    public Piso toEntity (PisoRequest request){
        return Piso.builder()
            .numero(request.getNumero())
            .descripcion(request.getDescripcion())
            .build();
    }

    // Transformar de Entidad MySQL a JSON
    public PisoResponse toResponse(Piso piso){
        return PisoResponse.builder()
            .id(piso.getId())
            .numero(piso.getNumero())
            .descripcion(piso.getDescripcion())
            .build();
    }
}
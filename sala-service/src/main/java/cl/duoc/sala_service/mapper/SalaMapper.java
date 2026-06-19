package cl.duoc.sala_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.sala_service.dto.PisoResponse;
import cl.duoc.sala_service.dto.SalaRequest;
import cl.duoc.sala_service.dto.SalaResponse;
import cl.duoc.sala_service.model.Sala;

@Component
public class SalaMapper {
    // Transformar de JSON a Entidad MySQL
    public Sala toEntity (SalaRequest request){
        return Sala.builder()
            .nombre(request.getNombre())
            .capacidad(request.getCapacidad())
            .idPiso(request.getIdPiso())
            .build();
    }

    // Transformar de Entidad MySQL a JSON
    public SalaResponse toResponse(Sala sala, PisoResponse pisoResponse){
        return SalaResponse.builder()
            .id(sala.getId())
            .nombre(sala.getNombre())
            .capacidad(sala.getCapacidad())
            .piso(pisoResponse)
            .build();
    }
}
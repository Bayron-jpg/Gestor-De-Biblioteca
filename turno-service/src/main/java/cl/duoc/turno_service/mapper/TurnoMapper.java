package cl.duoc.turno_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.turno_service.dto.BibliotecarioResponse;
import cl.duoc.turno_service.dto.TurnoRequest;
import cl.duoc.turno_service.dto.TurnoResponse;
import cl.duoc.turno_service.model.Turno;

@Component
public class TurnoMapper {
    // Transformar de JSON a Entidad MySQL
    public Turno toEntity (TurnoRequest request){
        return Turno.builder()
            .turno(request.getTurno())
            .horaEntrada(request.getHoraEntrada())
            .horaSalida(request.getHoraSalida())
            .idBibliotecario(request.getIdBibliotecario())
            .build();
    }
   
    // Transformar de Entidad MySQL a JSON
    public TurnoResponse toResponse(Turno turno, BibliotecarioResponse bibliResponse){
        return TurnoResponse.builder()
            .id(turno.getId())
            .turno(turno.getTurno())
            .horaEntrada(turno.getHoraEntrada())
            .horaSalida(turno.getHoraSalida())
            .bibliotecario(bibliResponse)
            .build();
    }
}
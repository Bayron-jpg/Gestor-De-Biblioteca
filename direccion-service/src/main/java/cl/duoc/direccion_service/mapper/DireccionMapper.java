package cl.duoc.direccion_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.direccion_service.dto.DireccionRequest;
import cl.duoc.direccion_service.dto.DireccionResponse;
import cl.duoc.direccion_service.model.Direccion;

@Component
public class DireccionMapper {
    // Transformar de JSON a Entidad MySQL
    public Direccion toEntity (DireccionRequest request){
        return Direccion.builder()
            .direccion(request.getDireccion())
            .comuna(request.getComuna())
            .region(request.getRegion())
            .build();
    }

    // Transformar de Entidad MySQL a JSON
    public DireccionResponse toResponse(Direccion direccion){
        return DireccionResponse.builder()
            .id(direccion.getId())
            .direccion(direccion.getDireccion())
            .comuna(direccion.getComuna())
            .region(direccion.getRegion())
            .build();
    }
}
package cl.duoc.libro_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.libro_service.dto.AutorResponse;
import cl.duoc.libro_service.dto.GeneroResponse;
import cl.duoc.libro_service.dto.LibroRequest;
import cl.duoc.libro_service.dto.LibroResponse;
import cl.duoc.libro_service.model.Libro;

@Component
public class LibroMapper {
    // Transformar de JSON a Entidad MySQL
    public Libro toEntity (LibroRequest request){
        return Libro.builder()
            .titulo(request.getTitulo())
            .isbn(request.getIsbn())
            .fechaPublicacion(request.getFechaPublicacion())
            .idAutor(request.getIdAutor())
            .idGenero(request.getIdGenero())
            .build();
    }

    // Transformar de Entidad MySQL a JSON
    public LibroResponse toResponse(Libro libro, AutorResponse autResponse, GeneroResponse genResponse){
        return LibroResponse.builder()
            .id(libro.getId())
            .titulo(libro.getTitulo())
            .isbn(libro.getIsbn())
            .fechaPublicacion(libro.getFechaPublicacion())
            .autor(autResponse)
            .genero(genResponse)
            .build();
    }
}
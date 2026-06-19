package cl.duoc.libro_service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibroResponse {
    private Long id;
    private String titulo;
    private String isbn;
    private LocalDate fechaPublicacion;
    private AutorResponse autor;
    private GeneroResponse genero;
}
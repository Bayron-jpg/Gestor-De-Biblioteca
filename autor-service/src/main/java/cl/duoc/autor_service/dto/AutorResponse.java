package cl.duoc.autor_service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AutorResponse {
    private Long id;
    private String nombre;
    private LocalDate fechaNacimiento;
}
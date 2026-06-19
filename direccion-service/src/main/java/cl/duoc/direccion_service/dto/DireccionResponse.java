package cl.duoc.direccion_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DireccionResponse {
    private Long id;
    private String direccion;
    private String comuna;
    private String region;
}
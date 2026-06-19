package cl.duoc.direccion_service.model;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "direccion")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String comuna;

    @Column(nullable = false)
    private String region;
}

package cl.duoc.genero_service.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cl.duoc.genero_service.dto.GeneroRequest;
import cl.duoc.genero_service.dto.GeneroResponse;
import cl.duoc.genero_service.model.Genero;
import cl.duoc.genero_service.service.GeneroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "GeneroV1", description = "Operaciones relacionadas con los géneros.")
@RequestMapping("/api/v1/generos")
@RestController
@Slf4j
public class GeneroControllerV1 {
    @Autowired
    private GeneroService service;

    @GetMapping
    @Operation(summary = "Obtener todos los géneros", description = "Obtiene los géneros existentes.")
    @ApiResponse(responseCode = "200", description = "Géneros obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Genero.class)))
    public ResponseEntity<List<GeneroResponse>> getGeneros() {
        log.info("V1/GET/generos");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener género por ID", description = "Obtiene un género por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Género obtenido exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Genero.class))),
            @ApiResponse(responseCode = "404", description = "Género no encontrado.")
    })
    public ResponseEntity<GeneroResponse> getGeneroPorId(@PathVariable Long id) {
        log.info("V1/GET/generos/{}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear género", description = "Crea un nuevo género.")
    @ApiResponse(responseCode = "201", description = "Género creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Genero.class)))
    public ResponseEntity<GeneroResponse> postGenero(@Valid @RequestBody GeneroRequest request) {
        log.info("V1/POST/generos");
        GeneroResponse generoCreado = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(generoCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(generoCreado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un género por su ID", description = "Modifica un género existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Género modificado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Genero.class))),
            @ApiResponse(responseCode = "404", description = "Género no encontrado.")
    })
    public ResponseEntity<GeneroResponse> putGenero(@PathVariable Long id,
            @Valid @RequestBody GeneroRequest request) {
        log.info("V1/PUT/generos/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar género por su ID", description = "Elimina un género existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Género eliminado(a).")
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id) {
        log.info("V1/DELETE/generos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
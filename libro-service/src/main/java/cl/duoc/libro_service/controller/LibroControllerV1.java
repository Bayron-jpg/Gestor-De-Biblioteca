package cl.duoc.libro_service.controller;

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

import cl.duoc.libro_service.dto.LibroRequest;
import cl.duoc.libro_service.dto.LibroResponse;
import cl.duoc.libro_service.model.Libro;
import cl.duoc.libro_service.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "LibrosV1", description = "Operaciones relacionadas con los libros.")
@RequestMapping("/api/v1/libros")
@RestController
@Slf4j
public class LibroControllerV1 {
    @Autowired
    private LibroService service;

    @GetMapping
    @Operation(summary = "Obtener todos los libros", description = "Obtiene los libros existentes.")
    @ApiResponse(responseCode = "200", description = "Libros obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class)))
    public ResponseEntity<List<LibroResponse>> getLibros() {
        log.info("V1/GET/libros");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener libro por ID", description = "Obtiene un libro por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro obtenido exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class))),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado.")
    })
    public ResponseEntity<LibroResponse> getLibroPorId(@PathVariable Long id) {
        log.info("V1/GET/libros/{}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear libro", description = "Crea un nuevo libro.")
    @ApiResponse(responseCode = "201", description = "Libro creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class)))
    public ResponseEntity<LibroResponse> postLibro(@Valid @RequestBody LibroRequest request) {
        log.info("V1/POST/libros");
        LibroResponse libroCreado = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(libroCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(libroCreado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un libro por su ID", description = "Modifica un libro existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro modificado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class))),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado.")
    })
    public ResponseEntity<LibroResponse> putLibro(@PathVariable Long id,
            @Valid @RequestBody LibroRequest request) {
        log.info("V1/PUT/libros/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar libro por su ID", description = "Elimina un libro existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Libro eliminado.")
    public ResponseEntity<Void> deleteLibro(@PathVariable Long id) {
        log.info("V1/DELETE/libros/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
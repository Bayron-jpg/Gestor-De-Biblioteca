package cl.duoc.autor_service.controller;

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

import cl.duoc.autor_service.dto.AutorRequest;
import cl.duoc.autor_service.dto.AutorResponse;
import cl.duoc.autor_service.model.Autor;
import cl.duoc.autor_service.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "AutoresV1", description = "Operaciones relacionadas con los autores.")
@RequestMapping("/api/v1/autores")
@RestController
@Slf4j
public class AutorControllerV1 {
    @Autowired
    private AutorService service;

    @GetMapping
    @Operation(summary = "Obtener todos los autores", description = "Obtiene los autores existentes.")
    @ApiResponse(responseCode = "200", description = "Autores obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class)))
    public ResponseEntity<List<AutorResponse>> getAutores() {
        log.info("V1/GET/autores");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener Autor por ID", description = "Obtiene un(a) autor(a) por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor(a) obtenido(a) exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class))),
            @ApiResponse(responseCode = "404", description = "Autor(a) no encontrado(a).")
    })
    public ResponseEntity<AutorResponse> getAutorPorId(@PathVariable Long id) {
        log.info("V1/GET/autores/{}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear Autor", description = "Crea un(a) nuevo(a) autor(a).")
    @ApiResponse(responseCode = "201", description = "Autor(a) creado(a) exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class)))
    public ResponseEntity<AutorResponse> postAutor(@Valid @RequestBody AutorRequest request) {
        log.info("V1/POST/autores");
        AutorResponse autorCreado = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autorCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(autorCreado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un autor por su ID", description = "Modifica un(a) autor(a) existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor(a) modificado(a) exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class))),
            @ApiResponse(responseCode = "404", description = "Autor(a) no encontrado(a).")
    })
    public ResponseEntity<AutorResponse> putAutor(@PathVariable Long id,
            @Valid @RequestBody AutorRequest request) {
        log.info("V1/PUT/autores/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar autor por su ID", description = "Elimina un(a) autor(a) existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Autor(a) eliminado(a).")
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id) {
        log.info("V1/DELETE/autores/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
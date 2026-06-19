package cl.duoc.bibliotecario_service.controller;

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

import cl.duoc.bibliotecario_service.dto.BibliotecarioRequest;
import cl.duoc.bibliotecario_service.dto.BibliotecarioResponse;
import cl.duoc.bibliotecario_service.model.Bibliotecario;
import cl.duoc.bibliotecario_service.service.BibliotecarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "BibliotecariosV1", description = "Operaciones relacionadas con los bibliotecarios.")
@RequestMapping("/api/v1/bibliotecarios")
@RestController
@Slf4j
public class BibliotecarioControllerV1 {
    @Autowired
    private BibliotecarioService service;

    @GetMapping
    @Operation(summary = "Obtener todos los bibliotecarios", description = "Obtiene los bibliotecarios existentes.")
    @ApiResponse(responseCode = "200", description = "Bibliotecarios obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Bibliotecario.class)))
    public ResponseEntity<List<BibliotecarioResponse>> getBibliotecarios() {
        log.info("V1/GET/bibliotecarios");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener bibliotecario por ID", description = "Obtiene un(a) bibliotecario(a) por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bibliotecario(a) obtenido(a) exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Bibliotecario.class))),
            @ApiResponse(responseCode = "404", description = "Bibliotecario(a) no encontrado(a).")
    })
    public ResponseEntity<BibliotecarioResponse> getBibliotecarioPorId(@PathVariable Long id) {
        log.info("V1/GET/bibliotecarios/{}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear bibliotecario", description = "Crea un(a) nuevo(a) bibliotecario(a).")
    @ApiResponse(responseCode = "201", description = "Bibliotecario(a) creado(a) exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Bibliotecario.class)))
    public ResponseEntity<BibliotecarioResponse> postBibliotecario (@Valid @RequestBody BibliotecarioRequest request) {
        log.info("V1/POST/bibliotecarios");
        BibliotecarioResponse bibliotecarioCreado = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bibliotecarioCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(bibliotecarioCreado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un bibliotecario por su ID", description = "Modifica un(a) bibliotecario(a) existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bibliotecario(a) modificado(a) exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Bibliotecario.class))),
            @ApiResponse(responseCode = "404", description = "Bibliotecario(a) no encontrado(a).")
    })
    public ResponseEntity<BibliotecarioResponse> putBibliotecario(@PathVariable Long id,
            @Valid @RequestBody BibliotecarioRequest request) {
        log.info("V1/PUT/bibliotecarios/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar bibliotecario por su ID", description = "Elimina un(a) bibliotecario(a) existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Bibliotecario(a) eliminado(a).")
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id) {
        log.info("V1/DELETE/bibliotecarios/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
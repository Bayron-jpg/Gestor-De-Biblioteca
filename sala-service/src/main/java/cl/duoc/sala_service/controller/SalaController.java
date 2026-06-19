package cl.duoc.sala_service.controller;

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

import cl.duoc.sala_service.dto.SalaRequest;
import cl.duoc.sala_service.dto.SalaResponse;
import cl.duoc.sala_service.model.Sala;
import cl.duoc.sala_service.service.SalaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "SalasV1", description = "Operaciones relacionadas con las salas.")
@RequestMapping("/api/v1/salas")
@RestController
@Slf4j
public class SalaController {
    @Autowired
    private SalaService service;

    @GetMapping
    @Operation(summary = "Obtener todas las salas", description = "Obtiene las salas existentes.")
    @ApiResponse(responseCode = "200", description = "Salas obtenidas exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sala.class)))
    public ResponseEntity<List<SalaResponse>> getSalas() {
        log.info("V1/GET/salas");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sala por ID", description = "Obtiene una sala por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala obtenida exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sala.class))),
            @ApiResponse(responseCode = "404", description = "Sala no encontrada.")
    })
    public ResponseEntity<SalaResponse> getSalaPorId(@PathVariable Long id) {
        log.info("V1/GET/salas/{}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear sala", description = "Crea una nueva sala.")
    @ApiResponse(responseCode = "201", description = "Sala creada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sala.class)))
    public ResponseEntity<SalaResponse> postSala(@Valid @RequestBody SalaRequest request) {
        log.info("V1/POST/salas");
        SalaResponse salaCreada = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salaCreada.getId())
                .toUri();

        return ResponseEntity.created(location).body(salaCreada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar una sala por su ID", description = "Modifica una sala existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala modificada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sala.class))),
            @ApiResponse(responseCode = "404", description = "Sala no encontrada.")
    })
    public ResponseEntity<SalaResponse> putSala(@PathVariable Long id,
            @Valid @RequestBody SalaRequest request) {
        log.info("V1/PUT/salas/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sala por su ID", description = "Elimina una sala existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Sala eliminada.")
    public ResponseEntity<Void> deleteSala(@PathVariable Long id) {
        log.info("V1/DELETE/salas/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
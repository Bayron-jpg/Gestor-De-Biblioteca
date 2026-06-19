package cl.duoc.turno_service.controller;

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

import cl.duoc.turno_service.dto.TurnoRequest;
import cl.duoc.turno_service.dto.TurnoResponse;
import cl.duoc.turno_service.model.Turno;
import cl.duoc.turno_service.service.TurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "TurnosV1", description = "Operaciones relacionadas con los turnos.")
@RequestMapping("/api/v1/turnos")
@RestController
@Slf4j
public class TurnoControllerV1 {

    @Autowired
    private TurnoService service;

    @GetMapping
    @Operation(summary = "Obtener todos los turnos", description = "Obtiene los turnos existentes.")
    @ApiResponse(responseCode = "200", description = "Turnos obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Turno.class)))
    public ResponseEntity<List<TurnoResponse>> getTurnos() {
        log.info("V1/GET/turnos");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener turno por ID", description = "Obtiene un turno por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno obtenido exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Turno.class))),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado.")
    })
    public ResponseEntity<TurnoResponse> getTurnoPorId(@PathVariable Long id) {
        log.info("V1/GET/turnos/{}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear turno", description = "Crea un nuevo turno.")
    @ApiResponse(responseCode = "201", description = "Turno creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Turno.class)))
    public ResponseEntity<TurnoResponse> postTurno(@Valid @RequestBody TurnoRequest request) {
        log.info("V1/POST/turnos");
        TurnoResponse turnoCreado = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(turnoCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(turnoCreado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un turno por su ID", description = "Modifica un turno existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno modificado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Turno.class))),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado.")
    })
    public ResponseEntity<TurnoResponse> putTurno(@PathVariable Long id,
            @Valid @RequestBody TurnoRequest request) {
        log.info("V1/PUT/turnos/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar turno por su ID", description = "Elimina un turno existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Turno eliminado.")
    public ResponseEntity<Void> deleteTurno(@PathVariable Long id) {
        log.info("V1/DELETE/turnos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

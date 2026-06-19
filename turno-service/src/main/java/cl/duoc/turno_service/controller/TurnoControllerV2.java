package cl.duoc.turno_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.turno_service.assemblers.TurnoModelAssembler;
import cl.duoc.turno_service.dto.TurnoRequest;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "TurnosV2", description = "Operaciones relacionadas con los Turnos.")
@RequestMapping("/api/v2/turnos")
@RestController
@Slf4j
public class TurnoControllerV2 {
    @Autowired
    private TurnoService service;

    @Autowired
    private TurnoModelAssembler assembler;

    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtener todos los turnos", description = "Obtiene los turnos existentes.")
    @ApiResponse(responseCode = "200", description = "Turnos obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Turno.class)))
    public CollectionModel<EntityModel<Turno>> getAll() {
        log.info("V2/GET/turnos");
        List<EntityModel<Turno>> turnos = service.getAllEntidad().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(turnos,
                linkTo(methodOn(TurnoControllerV2.class).getAll()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Obtener turno por su ID", description = "Obtiene un turno por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno obtenido exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Turno.class))),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado.")
    })
    public EntityModel<Turno> getTurnoPorId(@PathVariable Long id) {
        log.info("V2/GET/turnos/{}", id);
        Turno Turno = service.findByIdEntidad(id);
        return assembler.toModel(Turno);
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Crear turno", description = "Crea un nuevo turno.")
    @ApiResponse(responseCode = "201", description = "Turno creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Turno.class)))
    public ResponseEntity<EntityModel<Turno>> crearTurno(@Valid @RequestBody TurnoRequest request) {
        log.info("V2/POST/turnos");
        Turno nuevoTurno = service.crearEntidad(request);

        return ResponseEntity.created(
                linkTo(methodOn(TurnoControllerV2.class)
                        .getTurnoPorId(nuevoTurno.getId()))
                        .toUri())
                .body(assembler.toModel(nuevoTurno));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Modificar turno por su ID", description = "Modifica un turno existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno modificado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Turno.class))),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado.")
    })
    public ResponseEntity<EntityModel<Turno>> actualizarTurno(@PathVariable Long id,
            @Valid @RequestBody TurnoRequest request) {
        log.info("V2/PUT/turnos/{}", id);
        Turno turnoActualizado = service.actualizarEntidad(id, request);
        return ResponseEntity.ok(assembler.toModel(turnoActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Eliminar turno por ID", description = "Elimina un turno existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Turno eliminado.")
    public ResponseEntity<?> deleteTurno(@PathVariable Long id) {
        log.info("V2/DELETE/Turnos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}   
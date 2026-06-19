package cl.duoc.sala_service.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import cl.duoc.sala_service.assemblers.SalaModelAssembler;
import cl.duoc.sala_service.dto.SalaRequest;
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

@Tag(name = "SalasV2", description = "Operaciones relacionadas con las salas.")
@RequestMapping("/api/v2/salas")
@RestController
@Slf4j
public class SalaControllerV2 {

    @Autowired
    private SalaService service;

    @Autowired
    private SalaModelAssembler assembler;

    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtener todas las salas", description = "Obtiene las salas existentes.")
    @ApiResponse(responseCode = "200", description = "Salas obtenidas exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sala.class)))
    public CollectionModel<EntityModel<Sala>> getAll() {
        log.info("V2/GET/salas");
        List<EntityModel<Sala>> salas = service.getAllEntidad().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(salas,
                linkTo(methodOn(SalaControllerV2.class).getAll()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Obtener sala por su ID", description = "Obtiene una sala por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala obtenida exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sala.class))),
            @ApiResponse(responseCode = "404", description = "Sala no encontrada.")
    })
    public EntityModel<Sala> getSalaPorId(@PathVariable Long id) {
        log.info("V2/GET/salas/{}", id);
        Sala sala = service.findByIdEntidad(id);
        return assembler.toModel(sala);
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Crear sala", description = "Crea una nueva sala.")
    @ApiResponse(responseCode = "201", description = "Sala creada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sala.class)))
    public ResponseEntity<EntityModel<Sala>> crearSala(@Valid @RequestBody SalaRequest request) {
        log.info("V2/POST/salas");
        Sala nuevaSala = service.crearEntidad(request);

        return ResponseEntity.created(
                linkTo(methodOn(SalaControllerV2.class)
                        .getSalaPorId(nuevaSala.getId()))
                        .toUri())
                .body(assembler.toModel(nuevaSala));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Modificar sala por su ID", description = "Modifica una sala existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sala modificada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sala.class))),
            @ApiResponse(responseCode = "404", description = "Sala no encontrada.")
    })
    public ResponseEntity<EntityModel<Sala>> actualizarSala(@PathVariable Long id,
            @Valid @RequestBody SalaRequest request) {
        log.info("V2/PUT/salas/{}", id);
        Sala salaActualizada = service.actualizarEntidad(id, request);
        return ResponseEntity.ok(assembler.toModel(salaActualizada));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Eliminar sala por ID", description = "Elimina una sala existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Sala eliminada.")
    public ResponseEntity<?> deleteSala(@PathVariable Long id) {
        log.info("V2/DELETE/salas/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
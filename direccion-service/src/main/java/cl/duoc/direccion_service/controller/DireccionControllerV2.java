package cl.duoc.direccion_service.controller;

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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import cl.duoc.direccion_service.assemblers.DireccionModelAssembler;
import cl.duoc.direccion_service.dto.DireccionRequest;
import cl.duoc.direccion_service.model.Direccion;
import cl.duoc.direccion_service.service.DireccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "DireccionesV2", description = "Operaciones relacionadas con las direcciones.")
@RequestMapping("/api/v2/direcciones")
@RestController
@Slf4j
public class DireccionControllerV2 {
    @Autowired
    private DireccionService service;

    @Autowired
    private DireccionModelAssembler assembler;

    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtener todas las direcciones", description = "Obtiene las direcciones existentes.")
    @ApiResponse(responseCode = "200", description = "Direcciones obtenidas exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Direccion.class)))
    public CollectionModel<EntityModel<Direccion>> getAll() {
        log.info("V2/GET/direcciones");
        List<EntityModel<Direccion>> direcciones = service.getAllEntidad().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(direcciones,
                linkTo(methodOn(DireccionControllerV2.class).getAll()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Obtener dirección por su ID", description = "Obtiene una dirección por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dirección obtenida exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Direccion.class))),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada.")
    })
    public EntityModel<Direccion> getDireccionPorId(@PathVariable Long id) {
        log.info("V2/GET/direcciones/{}", id);
        Direccion direccion = service.findByIdEntidad(id);
        return assembler.toModel(direccion);
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Crear dirección", description = "Crea una nueva dirección.")
    @ApiResponse(responseCode = "201", description = "Dirección creada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Direccion.class)))
    public ResponseEntity<EntityModel<Direccion>> crearDireccion(@Valid @RequestBody DireccionRequest request) {
        log.info("V2/POST/direcciones");
        Direccion nuevaDireccion = service.crearEntidad(request);

        return ResponseEntity.created(
                linkTo(methodOn(DireccionControllerV2.class)
                        .getDireccionPorId(nuevaDireccion.getId()))
                        .toUri())
                .body(assembler.toModel(nuevaDireccion));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Modificar dirección por su ID", description = "Modifica una dirección existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dirección modificada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Direccion.class))),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada.")
    })
    public ResponseEntity<EntityModel<Direccion>> actualizarDireccion(@PathVariable Long id,
            @Valid @RequestBody DireccionRequest request) {
        log.info("V2/PUT/direcciones/{}", id);
        Direccion direccionActualizada = service.actualizarEntidad(id, request);
        return ResponseEntity.ok(assembler.toModel(direccionActualizada));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Eliminar dirección por ID", description = "Elimina una dirección existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Dirección eliminada.")
    public ResponseEntity<?> deleteDireccion(@PathVariable Long id) {
        log.info("V2/DELETE/direcciones/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
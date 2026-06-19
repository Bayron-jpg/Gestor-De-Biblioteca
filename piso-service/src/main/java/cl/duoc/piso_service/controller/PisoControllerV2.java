package cl.duoc.piso_service.controller;

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
import cl.duoc.piso_service.assemblers.PisoModelAssembler;
import cl.duoc.piso_service.dto.PisoRequest;
import cl.duoc.piso_service.model.Piso;
import cl.duoc.piso_service.service.PisoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "PisosV2", description = "Operaciones relacionadas con los pisos.")
@RequestMapping("/api/v2/pisos")
@RestController
@Slf4j
public class PisoControllerV2 {
    @Autowired
    private PisoService service;

    @Autowired
    private PisoModelAssembler assembler;

    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtener todos los pisos", description = "Obtiene los pisos existentes.")
    @ApiResponse(responseCode = "200", description = "Pisos obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piso.class)))
    public CollectionModel<EntityModel<Piso>> getAll() {
        log.info("V2/GET/pisos");
        List<EntityModel<Piso>> pisos = service.getAllEntidad().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(pisos,
                linkTo(methodOn(PisoControllerV2.class).getAll()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Obtener piso por su ID", description = "Obtiene un piso por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Piso obtenido exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piso.class))),
            @ApiResponse(responseCode = "404", description = "Piso no encontrado.")
    })
    public EntityModel<Piso> getPisoPorId(@PathVariable Long id) {
        log.info("V2/GET/pisos/{}", id);
        Piso piso = service.findByIdEntidad(id);
        return assembler.toModel(piso);
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Crear piso", description = "Crea un nuevo piso.")
    @ApiResponse(responseCode = "201", description = "Piso creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piso.class)))
    public ResponseEntity<EntityModel<Piso>> crearPiso(@Valid @RequestBody PisoRequest request) {
        log.info("V2/POST/pisos");
        Piso nuevoPiso = service.crearEntidad(request);

        return ResponseEntity.created(
                linkTo(methodOn(PisoControllerV2.class)
                        .getPisoPorId(nuevoPiso.getId()))
                        .toUri())
                .body(assembler.toModel(nuevoPiso));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Modificar piso por su ID", description = "Modifica un piso existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Piso modificado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piso.class))),
            @ApiResponse(responseCode = "404", description = "Piso no encontrado.")
    })
    public ResponseEntity<EntityModel<Piso>> actualizarPiso(@PathVariable Long id,
            @Valid @RequestBody PisoRequest request) {
        log.info("V2/PUT/pisos/{}", id);
        Piso pisoActualizado = service.actualizarEntidad(id, request);
        return ResponseEntity.ok(assembler.toModel(pisoActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Eliminar piso por ID", description = "Elimina un piso existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Piso eliminado.")
    public ResponseEntity<?> deletePiso(@PathVariable Long id) {
        log.info("V2/DELETE/pisos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
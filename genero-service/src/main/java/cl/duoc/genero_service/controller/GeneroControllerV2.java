package cl.duoc.genero_service.controller;

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

import cl.duoc.genero_service.assemblers.GeneroModelAssembler;
import cl.duoc.genero_service.dto.GeneroRequest;
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

@Tag(name = "GeneroV2", description = "Operaciones relacionadas con los géneros.")
@RequestMapping("/api/v2/generos")
@RestController
@Slf4j
public class GeneroControllerV2 {
    @Autowired
    private GeneroService service;

    @Autowired
    private GeneroModelAssembler assembler;

    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtener todos los géneros", description = "Obtiene los géneros existentes.")
    @ApiResponse(responseCode = "200", description = "Géneros obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Genero.class)))
    public CollectionModel<EntityModel<Genero>> getAll() {
        log.info("V2/GET/generos");
        List<EntityModel<Genero>> generos = service.getAllEntidad().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(generos,
                linkTo(methodOn(GeneroControllerV2.class).getAll()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Obtener género por su ID", description = "Obtiene un género por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Género obtenido exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Genero.class))),
            @ApiResponse(responseCode = "404", description = "Género no encontrado.")
    })
    public EntityModel<Genero> getGeneroPorId(@PathVariable Long id) {
        log.info("V2/GET/generos/{}", id);
        Genero genero = service.findByIdEntidad(id);
        return assembler.toModel(genero);
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Crear género", description = "Crea un nuevo género.")
    @ApiResponse(responseCode = "201", description = "Género creado) exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Genero.class)))
    public ResponseEntity<EntityModel<Genero>> crearGenero(@Valid @RequestBody GeneroRequest request) {
        log.info("V2/POST/generos");
        Genero nuevoGenero = service.crearEntidad(request);

        return ResponseEntity.created(
                linkTo(methodOn(GeneroControllerV2.class)
                        .getGeneroPorId(nuevoGenero.getId()))
                        .toUri())
                .body(assembler.toModel(nuevoGenero));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Modificar género por su ID", description = "Modifica un género existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Género modificado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Genero.class))),
            @ApiResponse(responseCode = "404", description = "Género no encontrado.")
    })
    public ResponseEntity<EntityModel<Genero>> actualizarGenero(@PathVariable Long id,
            @Valid @RequestBody GeneroRequest request) {
        log.info("V2/PUT/generos/{}", id);
        Genero generoActualizado = service.actualizarEntidad(id, request);
        return ResponseEntity.ok(assembler.toModel(generoActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Eliminar género por ID", description = "Elimina un género existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Género eliminado.")
    public ResponseEntity<?> deleteGenero(@PathVariable Long id) {
        log.info("V2/DELETE/generos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
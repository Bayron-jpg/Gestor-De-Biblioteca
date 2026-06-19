package cl.duoc.autor_service.controller;

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
import cl.duoc.autor_service.assemblers.AutorModelAssembler;
import cl.duoc.autor_service.dto.AutorRequest;
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

@Tag(name = "AutoresV2", description = "Operaciones relacionadas con los autores.")
@RequestMapping("/api/v2/autores")
@RestController
@Slf4j
public class AutorControllerV2 {
    @Autowired
    private AutorService service;

    @Autowired
    private AutorModelAssembler assembler;

    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtener todos los autores", description = "Obtiene los autores existentes.")
    @ApiResponse(responseCode = "200", description = "Autores obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class)))
    public CollectionModel<EntityModel<Autor>> getAll() {
        log.info("V2/GET/autores");
        List<EntityModel<Autor>> autores = service.getAllEntidad().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(autores,
                linkTo(methodOn(AutorControllerV2.class).getAll()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Obtener autor por su ID", description = "Obtiene un(a) autor(a) por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor(a) obtenido(a) exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class))),
            @ApiResponse(responseCode = "404", description = "Autor(a) no encontrado(a).")
    })
    public EntityModel<Autor> getAutorPorId(@PathVariable Long id) {
        log.info("V2/GET/autores/{}", id);
        Autor autor = service.findByIdEntidad(id);
        return assembler.toModel(autor);
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Crear autor", description = "Crea un(a) nuevo(a) autor(a).")
    @ApiResponse(responseCode = "201", description = "Autor(a) creado(a) exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class)))
    public ResponseEntity<EntityModel<Autor>> crearAutor(@Valid @RequestBody AutorRequest request) {
        log.info("V2/POST/autores");
        Autor nuevoAutor = service.crearEntidad(request);

        return ResponseEntity.created(
                linkTo(methodOn(AutorControllerV2.class)
                        .getAutorPorId(nuevoAutor.getId()))
                        .toUri())
                .body(assembler.toModel(nuevoAutor));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Modificar autor por su ID", description = "Modifica un(a) autor(a) existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor(a) modificado(a) exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class))),
            @ApiResponse(responseCode = "404", description = "Autor(a) no encontrado(a).")
    })
    public ResponseEntity<EntityModel<Autor>> actualizarAutor(@PathVariable Long id,
            @Valid @RequestBody AutorRequest request) {
        log.info("V2/PUT/autores/{}", id);
        Autor autorActualizado = service.actualizarEntidad(id, request);
        return ResponseEntity.ok(assembler.toModel(autorActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Eliminar autor por ID", description = "Elimina un(a) autor(a) existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Autor(a) eliminado(a).")
    public ResponseEntity<?> deleteAutor(@PathVariable Long id) {
        log.info("V2/DELETE/autores/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
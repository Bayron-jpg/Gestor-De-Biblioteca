package cl.duoc.bibliotecario_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
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

import cl.duoc.bibliotecario_service.assemblers.BibliotecarioModelAssembler;
import cl.duoc.bibliotecario_service.dto.BibliotecarioRequest;
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

@Tag(name = "BibliotecariosV2", description = "Operaciones relacionadas con los bibliotecarios.")
@RequestMapping("/api/v2/bibliotecarios")
@RestController
@Slf4j
public class BibliotecarioControllerV2 {
    @Autowired
    private BibliotecarioService service;    

    @Autowired
    private BibliotecarioModelAssembler assembler;

    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtener todos los bibliotecarios", description = "Obtiene los bibliotecarios existentes.")
    @ApiResponse(responseCode = "200", description = "Bibliotecarios obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Bibliotecario.class)))
    public CollectionModel<EntityModel<Bibliotecario>> getAll() {
        log.info("V2/GET/bibliotecarios");
        List<EntityModel<Bibliotecario>> bibliotecarios = service.getAllEntidad().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(bibliotecarios,
                linkTo(methodOn(BibliotecarioControllerV2.class).getAll()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Obtener bibliotecario por ID", description = "Obtiene un(a) Bibliotecario(a) por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bibliotecario(a) obtenido(a) exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Bibliotecario.class))),
            @ApiResponse(responseCode = "404", description = "Bibliotecario(a) no encontrado(a).")
    })
    public EntityModel<Bibliotecario> getBibliotecarioPorId(@PathVariable Long id) {
        log.info("V2/GET/bibliotecarios/{}", id);
        Bibliotecario bibliotecario = service.findByIdEntidad(id);
        return assembler.toModel(bibliotecario);
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Crear bibliotecario", description = "Crea un(a) nuevo(a) Bibliotecario(a).")
    @ApiResponse(responseCode = "201", description = "Bibliotecario(a) creado(a) exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Bibliotecario.class)))
    public ResponseEntity<EntityModel<Bibliotecario>> crearBibliotecario(@Valid @RequestBody BibliotecarioRequest request) {
        log.info("V2/POST/bibliotecarios");
        Bibliotecario nuevoBibliotecario = service.crearEntidad(request);

        return ResponseEntity.created(
                linkTo(methodOn(BibliotecarioControllerV2.class)
                        .getBibliotecarioPorId(nuevoBibliotecario.getId()))
                        .toUri())
                .body(assembler.toModel(nuevoBibliotecario));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Modificar bibliotecario por ID", description = "Modifica un(a) bibliotecario(a) existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bibliotecario(a) modificado(a) exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Bibliotecario.class))),
            @ApiResponse(responseCode = "404", description = "Bibliotecario(a) no encontrado(a).")
    })
    public ResponseEntity<EntityModel<Bibliotecario>> actualizarBibliotecario(@PathVariable Long id,
            @Valid @RequestBody BibliotecarioRequest request) {
        log.info("V2/PUT/bibliotecarios/{}", id);
        Bibliotecario bibliotecarioActualizado = service.actualizarEntidad(id, request);
        return ResponseEntity.ok(assembler.toModel(bibliotecarioActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Eliminar bibliotecario por ID", description = "Elimina un(a) bibliotecario(a) existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Bibliotecario(a) eliminado(a).")
    public ResponseEntity<?> deleteBibliotecario(@PathVariable Long id) {
        log.info("V2/DELETE/bibliotecarios/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
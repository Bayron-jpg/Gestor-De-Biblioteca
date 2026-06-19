package cl.duoc.libro_service.controller;

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

import cl.duoc.libro_service.assemblers.LibroModelAssembler;
import cl.duoc.libro_service.dto.LibroRequest;
import cl.duoc.libro_service.model.Libro;
import cl.duoc.libro_service.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "LibrosV2", description = "Operaciones relacionadas con los libros.")
@RequestMapping("/api/v2/libros")
@RestController
@Slf4j
public class LibroControllerV2 {

    @Autowired
    private LibroService service;

    @Autowired
    private LibroModelAssembler assembler;

    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtener todos los libros", description = "Obtiene los libros existentes.")
    @ApiResponse(responseCode = "200", description = "Libros obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class)))
    public CollectionModel<EntityModel<Libro>> getAll() {
        log.info("V2/GET/libros");
        List<EntityModel<Libro>> libros = service.getAllEntidad().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(libros,
                linkTo(methodOn(LibroControllerV2.class).getAll()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Obtener libro por su ID", description = "Obtiene un libro por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro obtenido exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class))),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado.")
    })
    public EntityModel<Libro> getLibroPorId(@PathVariable Long id) {
        log.info("V2/GET/libros/{}", id);
        Libro libro = service.findByIdEntidad(id);
        return assembler.toModel(libro);
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Crear libro", description = "Crea un nuevo libro.")
    @ApiResponse(responseCode = "201", description = "Libro creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class)))
    public ResponseEntity<EntityModel<Libro>> crearLibro(@Valid @RequestBody LibroRequest request) {
        log.info("V2/POST/libros");
        Libro nuevoLibro = service.crearEntidad(request);

        return ResponseEntity.created(
                linkTo(methodOn(LibroControllerV2.class)
                        .getLibroPorId(nuevoLibro.getId()))
                        .toUri())
                .body(assembler.toModel(nuevoLibro));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Modificar libro por su ID", description = "Modifica un libro existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro modificado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class))),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado.")
    })
    public ResponseEntity<EntityModel<Libro>> actualizarLibro(@PathVariable Long id,
            @Valid @RequestBody LibroRequest request) {
        log.info("V2/PUT/libros/{}", id);
        Libro libroActualizado = service.actualizarEntidad(id, request);
        return ResponseEntity.ok(assembler.toModel(libroActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Eliminar libro por ID", description = "Elimina un libro existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Libro eliminado.")
    public ResponseEntity<?> deleteLibro(@PathVariable Long id) {
        log.info("V2/DELETE/libros/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
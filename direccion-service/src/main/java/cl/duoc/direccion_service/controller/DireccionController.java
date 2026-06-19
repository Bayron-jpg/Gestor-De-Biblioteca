package cl.duoc.direccion_service.controller;

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

import cl.duoc.direccion_service.dto.DireccionRequest;
import cl.duoc.direccion_service.dto.DireccionResponse;
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

@Tag(name = "DireccionesV1", description = "Operaciones relacionadas con las direcciones.")
@RequestMapping("/api/v1/direcciones")
@RestController
@Slf4j
public class DireccionController {
    @Autowired
    private DireccionService service;

    @GetMapping
    @Operation(summary = "Obtener todas las direcciones", description = "Obtiene las direcciones existentes.")
    @ApiResponse(responseCode = "200", description = "Direcciones obtenidas exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Direccion.class)))
    public ResponseEntity<List<DireccionResponse>> getDirecciones() {
        log.info("V1/GET/direcciones");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener dirección por ID", description = "Obtiene una dirección por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dirección obtenida exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Direccion.class))),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada.")
    })
    public ResponseEntity<DireccionResponse> getDireccionPorId(@PathVariable Long id) {
        log.info("V1/GET/direcciones/{}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear dirección", description = "Crea una nueva dirección.")
    @ApiResponse(responseCode = "201", description = "Dirección creada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Direccion.class)))
    public ResponseEntity<DireccionResponse> postDireccion(@Valid @RequestBody DireccionRequest request) {
        log.info("V1/POST/direcciones");
        DireccionResponse direccionCreada = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(direccionCreada.getId())
                .toUri();

        return ResponseEntity.created(location).body(direccionCreada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar una dirección por su ID", description = "Modifica una dirección existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dirección modificada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Direccion.class))),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada.")
    })
    public ResponseEntity<DireccionResponse> putDireccion(@PathVariable Long id,
            @Valid @RequestBody DireccionRequest request) {
        log.info("V1/PUT/direcciones/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar dirección por su ID", description = "Elimina una dirección existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Dirección eliminada.")
    public ResponseEntity<Void> deleteDireccion(@PathVariable Long id) {
        log.info("V1/DELETE/direcciones/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
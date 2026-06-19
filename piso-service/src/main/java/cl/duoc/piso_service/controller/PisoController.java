package cl.duoc.piso_service.controller;

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

import cl.duoc.piso_service.dto.PisoRequest;
import cl.duoc.piso_service.dto.PisoResponse;
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

@Tag(name = "PisosV1", description = "Operaciones relacionadas con los pisos.")
@RequestMapping("/api/v1/pisos")
@RestController
@Slf4j
public class PisoController {
    @Autowired
    private PisoService service;

    @GetMapping
    @Operation(summary = "Obtener todos los pisos", description = "Obtiene los pisos existentes.")
    @ApiResponse(responseCode = "200", description = "Pisos obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piso.class)))
    public ResponseEntity<List<PisoResponse>> getPisos() {
        log.info("V1/GET/pisos");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener piso por ID", description = "Obtiene un piso por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Piso obtenido exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piso.class))),
            @ApiResponse(responseCode = "404", description = "Piso no encontrado.")
    })
    public ResponseEntity<PisoResponse> getPisoPorId(@PathVariable Long id) {
        log.info("V1/GET/pisos/{}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear piso", description = "Crea un nuevo piso.")
    @ApiResponse(responseCode = "201", description = "Piso creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piso.class)))
    public ResponseEntity<PisoResponse> postPiso(@Valid @RequestBody PisoRequest request) {
        log.info("V1/POST/pisos");
        PisoResponse pisoCreado = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pisoCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(pisoCreado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un piso por su ID", description = "Modifica un piso existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Piso modificado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piso.class))),
            @ApiResponse(responseCode = "404", description = "Piso no encontrado.")
    })
    public ResponseEntity<PisoResponse> putPiso(@PathVariable Long id,
            @Valid @RequestBody PisoRequest request) {
        log.info("V1/PUT/pisos/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar piso por su ID", description = "Elimina un piso existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Piso eliminado.")
    public ResponseEntity<Void> deletePiso(@PathVariable Long id) {
        log.info("V1/DELETE/pisos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
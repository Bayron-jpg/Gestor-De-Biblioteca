package cl.duoc.direccion_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.duoc.direccion_service.assemblers.DireccionModelAssembler;
import cl.duoc.direccion_service.dto.DireccionRequest;
import cl.duoc.direccion_service.model.Direccion;
import cl.duoc.direccion_service.service.DireccionService;

@WebMvcTest(DireccionControllerV2.class)
@ActiveProfiles("test")
public class DireccionControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DireccionService service;

    @MockitoBean
    private DireccionModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Direccion direccion;
    private DireccionRequest request;
    private EntityModel<Direccion> entityModel;

    @BeforeEach
    void setUp() {
        direccion = Direccion.builder()
                .id(1L)
                .direccion("Av. Providencia 123")
                .comuna("Providencia")
                .region("Metropolitana")
                .build();

        request = DireccionRequest.builder()
                .direccion("Av. Providencia 123")
                .comuna("Providencia")
                .region("Metropolitana")
                .build();

        entityModel = EntityModel.of(direccion);
    }

    @Test
    void testGetAll() throws Exception {
        when(service.getAllEntidad()).thenReturn(List.of(direccion));
        when(assembler.toModel(direccion)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/direcciones"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        when(service.findByIdEntidad(1L)).thenReturn(direccion);
        when(assembler.toModel(direccion)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/direcciones/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrear() throws Exception {
        when(service.crearEntidad(any(DireccionRequest.class)))
                .thenReturn(direccion);

        when(assembler.toModel(direccion))
                .thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/direcciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizar() throws Exception {
        when(service.actualizarEntidad(eq(1L), any(DireccionRequest.class)))
                .thenReturn(direccion);

        when(assembler.toModel(direccion))
                .thenReturn(entityModel);

        mockMvc.perform(put("/api/v2/direcciones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v2/direcciones/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
package cl.duoc.sala_service.controller;

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

import cl.duoc.sala_service.assemblers.SalaModelAssembler;
import cl.duoc.sala_service.dto.SalaRequest;
import cl.duoc.sala_service.model.Sala;
import cl.duoc.sala_service.service.SalaService;

@WebMvcTest(SalaControllerV2.class)
@ActiveProfiles("test")
public class SalaControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SalaService service;

    @MockitoBean
    private SalaModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Sala sala;
    private SalaRequest request;
    private EntityModel<Sala> entityModel;

    @BeforeEach
    void setUp() {
        sala = Sala.builder()
                .id(1L)
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();

        request = SalaRequest.builder()
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();

        entityModel = EntityModel.of(sala);
    }

    @Test
    void testGetAll() throws Exception {
        when(service.getAllEntidad()).thenReturn(List.of(sala));
        when(assembler.toModel(sala)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/salas"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        when(service.findByIdEntidad(1L)).thenReturn(sala);
        when(assembler.toModel(sala)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/salas/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrear() throws Exception {
        when(service.crearEntidad(any(SalaRequest.class)))
                .thenReturn(sala);

        when(assembler.toModel(sala))
                .thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/salas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizar() throws Exception {
        when(service.actualizarEntidad(eq(1L), any(SalaRequest.class)))
                .thenReturn(sala);

        when(assembler.toModel(sala))
                .thenReturn(entityModel);

        mockMvc.perform(put("/api/v2/salas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v2/salas/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.duoc.sala_service.dto.PisoResponse;
import cl.duoc.sala_service.dto.SalaRequest;
import cl.duoc.sala_service.dto.SalaResponse;
import cl.duoc.sala_service.service.SalaService;

@WebMvcTest(SalaController.class)
@ActiveProfiles("test")
public class SalaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SalaService service;

    @Autowired
    private ObjectMapper objectMapper;

    private SalaResponse response;
    private SalaRequest request;

    @BeforeEach
    void setUp() {
        PisoResponse piso = new PisoResponse(1L, "1", "Primer piso");

        response = SalaResponse.builder()
                .id(1L)
                .nombre("Sala A101")
                .capacidad(30)
                .piso(piso)
                .build();

        request = SalaRequest.builder()
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();
    }

    @Test
    public void testGetAll() throws Exception {

        when(service.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/salas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Sala A101"));
    }

    @Test
    public void testGetById() throws Exception {

        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/salas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Sala A101"));
    }

    @Test
    public void testCrear() throws Exception {

        when(service.crear(any(SalaRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/salas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Sala A101"));
    }

    @Test
    public void testActualizar() throws Exception {

        when(service.actualizar(eq(1L), any(SalaRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/salas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sala A101"));
    }

    @Test
    public void testEliminar() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/salas/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
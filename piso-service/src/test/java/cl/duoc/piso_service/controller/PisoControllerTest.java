package cl.duoc.piso_service.controller;

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

import cl.duoc.piso_service.dto.PisoRequest;
import cl.duoc.piso_service.dto.PisoResponse;
import cl.duoc.piso_service.service.PisoService;

@WebMvcTest(PisoController.class)
@ActiveProfiles("test")
public class PisoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PisoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private PisoResponse response;
    private PisoRequest request;

    @BeforeEach
    void setUp() {
        response = PisoResponse.builder()
                .id(1L)
                .numero("1")
                .descripcion("Piso de administración.")
                .build();

        request = PisoRequest.builder()
                .numero("1")
                .descripcion("Piso de administración.")
                .build();
    }

    @Test
    public void testGetAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/pisos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].numero").value("1"));
    }

    @Test
    public void testGetById() throws Exception {
        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/pisos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numero").value("1"));
    }

    @Test
    public void testCrear() throws Exception {
        when(service.crear(any(PisoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/pisos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero").value("1"));
    }

    @Test
    public void testActualizar() throws Exception {
        when(service.actualizar(eq(1L), any(PisoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/pisos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value("1"));
    }

    @Test
    public void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/pisos/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
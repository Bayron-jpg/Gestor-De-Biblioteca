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

import cl.duoc.direccion_service.dto.DireccionRequest;
import cl.duoc.direccion_service.dto.DireccionResponse;
import cl.duoc.direccion_service.service.DireccionService;

@WebMvcTest(DireccionController.class)
@ActiveProfiles("test")
public class DireccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DireccionService service;

    @Autowired
    private ObjectMapper objectMapper;

    private DireccionResponse response;
    private DireccionRequest request;

    @BeforeEach
    void setUp() {
        response = DireccionResponse.builder()
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
    }

    @Test
    public void testGetAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/direcciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].direccion").value("Av. Providencia 123"));
    }

    @Test
    public void testGetById() throws Exception {
        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/direcciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.direccion").value("Av. Providencia 123"));
    }

    @Test
    public void testCrear() throws Exception {
        when(service.crear(any(DireccionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/direcciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.direccion").value("Av. Providencia 123"));
    }

    @Test
    public void testActualizar() throws Exception {
        when(service.actualizar(eq(1L), any(DireccionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/direcciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccion").value("Av. Providencia 123"));
    }

    @Test
    public void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/direcciones/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
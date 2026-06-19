package cl.duoc.genero_service.controller;

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

import cl.duoc.genero_service.dto.GeneroRequest;
import cl.duoc.genero_service.dto.GeneroResponse;
import cl.duoc.genero_service.service.GeneroService;

@WebMvcTest(GeneroControllerV1.class)
@ActiveProfiles("test")
public class GeneroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GeneroService service;

    @Autowired
    private ObjectMapper objectMapper;

    private GeneroResponse response;
    private GeneroRequest request;

    @BeforeEach
    void setUp() {
        response = GeneroResponse.builder()
                .id(1L)
                .nombre("Fantasía")
                .descripcion("Historias con elementos mágicos o sobrenaturales.")
                .build();

        request = GeneroRequest.builder()
                .nombre("Fantasía")
                .descripcion("Historias con elementos mágicos o sobrenaturales.")
                .build();
    }

    @Test
    public void testGetAll() throws Exception {

        when(service.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/generos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Fantasía"));
    }

    @Test
    public void testGetById() throws Exception {

        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/generos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Fantasía"));
    }

    @Test
    public void testCrear() throws Exception {

        when(service.crear(any(GeneroRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/generos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Fantasía"));
    }

    @Test
    public void testActualizar() throws Exception {

        when(service.actualizar(eq(1L), any(GeneroRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/generos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Fantasía"));
    }

    @Test
    public void testEliminar() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/generos/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
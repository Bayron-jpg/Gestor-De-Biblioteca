package cl.duoc.autor_service.controller;

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

import java.time.LocalDate;
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

import cl.duoc.autor_service.dto.AutorRequest;
import cl.duoc.autor_service.dto.AutorResponse;
import cl.duoc.autor_service.service.AutorService;

@WebMvcTest(AutorControllerV1.class)
@ActiveProfiles("test")
public class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutorService service;

    @Autowired
    private ObjectMapper objectMapper;

    private AutorResponse response;
    private AutorRequest request;

    @BeforeEach
    void setUp() {
        response = AutorResponse.builder()
                .id(1L)
                .nombre("Claudio Irarrazabal")
                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                .build();

        request = AutorRequest.builder()
                .nombre("Claudio Irarrazabal")
                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                .build();
    }

    @Test
    public void testGetAll() throws Exception {

        when(service.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Claudio Irarrazabal"));
    }

    @Test
    public void testGetById() throws Exception {

        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/autores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Claudio Irarrazabal"));
    }

    @Test
    public void testCrear() throws Exception {

        when(service.crear(any(AutorRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Claudio Irarrazabal"));
    }

    @Test
    public void testActualizar() throws Exception {

        when(service.actualizar(eq(1L), any(AutorRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/autores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Claudio Irarrazabal"));
    }

    @Test
    public void testEliminar() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/autores/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
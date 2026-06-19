package cl.duoc.telefono_service.controller;

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

import cl.duoc.telefono_service.dto.TelefonoRequest;
import cl.duoc.telefono_service.dto.TelefonoResponse;
import cl.duoc.telefono_service.service.TelefonoService;

@WebMvcTest(TelefonoController.class)
@ActiveProfiles("test")
public class TelefonoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TelefonoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private TelefonoResponse response;
    private TelefonoRequest request;

    @BeforeEach
    void setUp() {
        response = TelefonoResponse.builder()
                .id(1L)
                .numero(987654321)
                .tipo("Móvil")
                .build();

        request = TelefonoRequest.builder()
                .numero(987654321)
                .tipo("Móvil")
                .build();
    }

    @Test
    public void testGetAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/telefonos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].numero").value(987654321));
    }

    @Test
    public void testGetById() throws Exception {
        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/telefonos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numero").value(987654321));
    }

    @Test
    public void testCrear() throws Exception {
        when(service.crear(any(TelefonoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/telefonos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero").value(987654321));
    }

    @Test
    public void testActualizar() throws Exception {
        when(service.actualizar(eq(1L), any(TelefonoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/telefonos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(987654321));
    }

    @Test
    public void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/telefonos/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
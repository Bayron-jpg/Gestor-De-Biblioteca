package cl.duoc.turno_service.controller;

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

import java.time.LocalTime;
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

import cl.duoc.turno_service.dto.BibliotecarioResponse;
import cl.duoc.turno_service.dto.TurnoRequest;
import cl.duoc.turno_service.dto.TurnoResponse;
import cl.duoc.turno_service.service.TurnoService;

@WebMvcTest(TurnoControllerV1.class)
@ActiveProfiles("test")
public class TurnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TurnoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private TurnoResponse response;
    private TurnoRequest request;

    @BeforeEach
    void setUp() {
        BibliotecarioResponse bibliotecario = new BibliotecarioResponse(1L, "Carla", "Soto", 35);

        response = TurnoResponse.builder()
                .id(1L)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .bibliotecario(bibliotecario)
                .build();

        request = TurnoRequest.builder()
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();
    }

    @Test
    public void testGetAll() throws Exception {

        when(service.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/turnos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].turno").value("Mañana"));
    }

    @Test
    public void testGetById() throws Exception {

        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/turnos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.turno").value("Mañana"));
    }

    @Test
    public void testCrear() throws Exception {

        when(service.crear(any(TurnoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.turno").value("Mañana"));
    }

    @Test
    public void testActualizar() throws Exception {

        when(service.actualizar(eq(1L), any(TurnoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/turnos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turno").value("Mañana"));
    }

    @Test
    public void testEliminar() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/turnos/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
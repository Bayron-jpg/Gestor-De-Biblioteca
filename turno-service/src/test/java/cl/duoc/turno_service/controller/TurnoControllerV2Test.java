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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
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

import cl.duoc.turno_service.assemblers.TurnoModelAssembler;
import cl.duoc.turno_service.dto.TurnoRequest;
import cl.duoc.turno_service.model.Turno;
import cl.duoc.turno_service.service.TurnoService;

@WebMvcTest(TurnoControllerV2.class)
@ActiveProfiles("test")
public class TurnoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TurnoService service;

    @MockitoBean
    private TurnoModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Turno turno;
    private TurnoRequest request;
    private EntityModel<Turno> entityModel;

    @BeforeEach
    void setUp() {
        turno = Turno.builder()
                .id(1L)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        request = TurnoRequest.builder()
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        entityModel = EntityModel.of(turno);
    }

    @Test
    void testGetAll() throws Exception {
        when(service.getAllEntidad()).thenReturn(List.of(turno));
        when(assembler.toModel(turno)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/turnos"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        when(service.findByIdEntidad(1L)).thenReturn(turno);
        when(assembler.toModel(turno)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/turnos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrear() throws Exception {
        when(service.crearEntidad(any(TurnoRequest.class)))
                .thenReturn(turno);

        when(assembler.toModel(turno))
                .thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/turnos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizar() throws Exception {
        when(service.actualizarEntidad(eq(1L), any(TurnoRequest.class)))
                .thenReturn(turno);

        when(assembler.toModel(turno))
                .thenReturn(entityModel);

        mockMvc.perform(put("/api/v2/turnos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v2/turnos/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
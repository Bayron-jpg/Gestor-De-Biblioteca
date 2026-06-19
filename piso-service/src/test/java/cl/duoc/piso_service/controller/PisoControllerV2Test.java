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

import cl.duoc.piso_service.assemblers.PisoModelAssembler;
import cl.duoc.piso_service.dto.PisoRequest;
import cl.duoc.piso_service.model.Piso;
import cl.duoc.piso_service.service.PisoService;

@WebMvcTest(PisoControllerV2.class)
@ActiveProfiles("test")
public class PisoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PisoService service;

    @MockitoBean
    private PisoModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Piso piso;
    private PisoRequest request;
    private EntityModel<Piso> entityModel;

    @BeforeEach
    void setUp() {
        piso = Piso.builder()
                .id(1L)
                .numero("1")
                .descripcion("Piso de administración.")
                .build();

        request = PisoRequest.builder()
                .numero("1")
                .descripcion("Piso de administración.")
                .build();

        entityModel = EntityModel.of(piso);
    }

    @Test
    void testGetAll() throws Exception {
        when(service.getAllEntidad()).thenReturn(List.of(piso));
        when(assembler.toModel(piso)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/pisos"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        when(service.findByIdEntidad(1L)).thenReturn(piso);
        when(assembler.toModel(piso)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/pisos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrear() throws Exception {
        when(service.crearEntidad(any(PisoRequest.class)))
                .thenReturn(piso);

        when(assembler.toModel(piso))
                .thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/pisos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizar() throws Exception {
        when(service.actualizarEntidad(eq(1L), any(PisoRequest.class)))
                .thenReturn(piso);

        when(assembler.toModel(piso))
                .thenReturn(entityModel);

        mockMvc.perform(put("/api/v2/pisos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v2/pisos/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
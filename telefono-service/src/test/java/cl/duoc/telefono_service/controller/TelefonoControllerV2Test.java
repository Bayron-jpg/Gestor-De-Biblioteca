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

import cl.duoc.telefono_service.assemblers.TelefonoModelAssembler;
import cl.duoc.telefono_service.dto.TelefonoRequest;
import cl.duoc.telefono_service.model.Telefono;
import cl.duoc.telefono_service.service.TelefonoService;

@WebMvcTest(TelefonoControllerV2.class)
@ActiveProfiles("test")
public class TelefonoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TelefonoService service;

    @MockitoBean
    private TelefonoModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Telefono telefono;
    private TelefonoRequest request;
    private EntityModel<Telefono> entityModel;

    @BeforeEach
    void setUp() {
        telefono = Telefono.builder()
                .id(1L)
                .numero(987654321)
                .tipo("Móvil")
                .build();

        request = TelefonoRequest.builder()
                .numero(987654321)
                .tipo("Móvil")
                .build();

        entityModel = EntityModel.of(telefono);
    }

    @Test
    void testGetAll() throws Exception {
        when(service.getAllEntidad()).thenReturn(List.of(telefono));
        when(assembler.toModel(telefono)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/telefonos"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        when(service.findByIdEntidad(1L)).thenReturn(telefono);
        when(assembler.toModel(telefono)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/telefonos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrear() throws Exception {
        when(service.crearEntidad(any(TelefonoRequest.class)))
                .thenReturn(telefono);

        when(assembler.toModel(telefono))
                .thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/telefonos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizar() throws Exception {
        when(service.actualizarEntidad(eq(1L), any(TelefonoRequest.class)))
                .thenReturn(telefono);

        when(assembler.toModel(telefono))
                .thenReturn(entityModel);

        mockMvc.perform(put("/api/v2/telefonos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v2/telefonos/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
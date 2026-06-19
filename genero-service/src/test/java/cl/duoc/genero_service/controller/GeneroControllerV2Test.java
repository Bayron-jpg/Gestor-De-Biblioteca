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

import cl.duoc.genero_service.assemblers.GeneroModelAssembler;
import cl.duoc.genero_service.dto.GeneroRequest;
import cl.duoc.genero_service.model.Genero;
import cl.duoc.genero_service.service.GeneroService;

@WebMvcTest(GeneroControllerV2.class)
@ActiveProfiles("test")
public class GeneroControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GeneroService service;

    @MockitoBean
    private GeneroModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Genero genero;
    private GeneroRequest request;
    private EntityModel<Genero> entityModel;

    @BeforeEach
    void setUp() {
        genero = Genero.builder()
                .id(1L)
                .nombre("Fantasía")
                .descripcion("Historias con elementos mágicos o sobrenaturales.")
                .build();
        request = GeneroRequest.builder()
                .nombre("Fantasía")
                .descripcion("Historias con elementos mágicos o sobrenaturales.")
                .build();

        entityModel = EntityModel.of(genero);
    }

    @Test
    void testGetAll() throws Exception {
        when(service.getAllEntidad()).thenReturn(List.of(genero));
        when(assembler.toModel(genero)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/generos"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        when(service.findByIdEntidad(1L)).thenReturn(genero);
        when(assembler.toModel(genero)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/generos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrear() throws Exception {
        when(service.crearEntidad(any(GeneroRequest.class)))
                .thenReturn(genero);

        when(assembler.toModel(genero))
                .thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/generos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizar() throws Exception {
        when(service.actualizarEntidad(eq(1L), any(GeneroRequest.class)))
                .thenReturn(genero);

        when(assembler.toModel(genero))
                .thenReturn(entityModel);

        mockMvc.perform(put("/api/v2/generos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v2/generos/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
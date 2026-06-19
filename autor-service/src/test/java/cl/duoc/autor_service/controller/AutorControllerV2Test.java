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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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

import cl.duoc.autor_service.assemblers.AutorModelAssembler;
import cl.duoc.autor_service.dto.AutorRequest;
import cl.duoc.autor_service.model.Autor;
import cl.duoc.autor_service.service.AutorService;

@WebMvcTest(AutorControllerV2.class)
@ActiveProfiles("test")
public class AutorControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutorService service;

    @MockitoBean
    private AutorModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Autor autor;
    private AutorRequest request;
    private EntityModel<Autor> entityModel;

    @BeforeEach
    void setUp() {
        autor = Autor.builder()
                .id(1L)
                .nombre("Claudio Irarrazabal")
                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                .build();
        request = AutorRequest.builder()
                .nombre("Claudio Irarrazabal")
                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                .build();

        entityModel = EntityModel.of(autor);
    }

    @Test
    void testGetAll() throws Exception {
        when(service.getAllEntidad()).thenReturn(List.of(autor));
        when(assembler.toModel(autor)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/autores"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        when(service.findByIdEntidad(1L)).thenReturn(autor);
        when(assembler.toModel(autor)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/autores/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrear() throws Exception {
        when(service.crearEntidad(any(AutorRequest.class)))
                .thenReturn(autor);

        when(assembler.toModel(autor))
                .thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/autores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizar() throws Exception {
        when(service.actualizarEntidad(eq(1L), any(AutorRequest.class)))
                .thenReturn(autor);

        when(assembler.toModel(autor))
                .thenReturn(entityModel);

        mockMvc.perform(put("/api/v2/autores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v2/autores/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
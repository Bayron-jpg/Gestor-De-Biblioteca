package cl.duoc.libro_service.controller;

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

import cl.duoc.libro_service.assemblers.LibroModelAssembler;
import cl.duoc.libro_service.dto.LibroRequest;
import cl.duoc.libro_service.model.Libro;
import cl.duoc.libro_service.service.LibroService;

@WebMvcTest(LibroControllerV2.class)
@ActiveProfiles("test")
public class LibroControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibroService service;

    @MockitoBean
    private LibroModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Libro libro;
    private LibroRequest request;
    private EntityModel<Libro> entityModel;

    @BeforeEach
    void setUp() {
        libro = Libro.builder()
                .id(1L)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        entityModel = EntityModel.of(libro);
    }

    @Test
    void testGetAll() throws Exception {
        when(service.getAllEntidad()).thenReturn(List.of(libro));
        when(assembler.toModel(libro)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/libros"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        when(service.findByIdEntidad(1L)).thenReturn(libro);
        when(assembler.toModel(libro)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/libros/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrear() throws Exception {
        when(service.crearEntidad(any(LibroRequest.class)))
                .thenReturn(libro);

        when(assembler.toModel(libro))
                .thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizar() throws Exception {
        when(service.actualizarEntidad(eq(1L), any(LibroRequest.class)))
                .thenReturn(libro);

        when(assembler.toModel(libro))
                .thenReturn(entityModel);

        mockMvc.perform(put("/api/v2/libros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v2/libros/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
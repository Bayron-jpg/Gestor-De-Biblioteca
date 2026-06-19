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

import cl.duoc.libro_service.dto.AutorResponse;
import cl.duoc.libro_service.dto.GeneroResponse;
import cl.duoc.libro_service.dto.LibroRequest;
import cl.duoc.libro_service.dto.LibroResponse;
import cl.duoc.libro_service.service.LibroService;

@WebMvcTest(LibroControllerV1.class)
@ActiveProfiles("test")
public class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibroService service;

    @Autowired
    private ObjectMapper objectMapper;

    private LibroResponse response;
    private LibroRequest request;

    @BeforeEach
    void setUp() {
        AutorResponse autor = new AutorResponse(1L, "Gabriel García Márquez", LocalDate.of(1927, 3, 6));
        GeneroResponse genero = new GeneroResponse(1L, "Realismo mágico", "Mezcla de elementos fantásticos con la realidad.");

        response = LibroResponse.builder()
                .id(1L)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .autor(autor)
                .genero(genero)
                .build();

        request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();
    }

    @Test
    public void testGetAll() throws Exception {

        when(service.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Cien años de soledad"));
    }

    @Test
    public void testGetById() throws Exception {

        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Cien años de soledad"));
    }

    @Test
    public void testCrear() throws Exception {

        when(service.crear(any(LibroRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Cien años de soledad"));
    }

    @Test
    public void testActualizar() throws Exception {

        when(service.actualizar(eq(1L), any(LibroRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/libros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Cien años de soledad"));
    }

    @Test
    public void testEliminar() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/libros/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
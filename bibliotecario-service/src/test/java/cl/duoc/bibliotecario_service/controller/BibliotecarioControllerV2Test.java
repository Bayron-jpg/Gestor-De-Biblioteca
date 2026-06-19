package cl.duoc.bibliotecario_service.controller;

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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.duoc.bibliotecario_service.assemblers.BibliotecarioModelAssembler;
import cl.duoc.bibliotecario_service.dto.BibliotecarioRequest;
import cl.duoc.bibliotecario_service.model.Bibliotecario;
import cl.duoc.bibliotecario_service.service.BibliotecarioService;

@WebMvcTest(BibliotecarioControllerV2.class)
@ActiveProfiles("test")
public class BibliotecarioControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BibliotecarioService service;

    @MockitoBean
    private BibliotecarioModelAssembler assembler; // Mockeamos el assembler para aislar el controlador

    @Autowired
    private ObjectMapper objectMapper;

    private Bibliotecario bibliotecario;
    private EntityModel<Bibliotecario> entityModel;
    private BibliotecarioRequest request;

    @BeforeEach
    void setUp() {
        // La entidad de negocio real
        bibliotecario = new Bibliotecario();
        bibliotecario.setId(1L);
        bibliotecario.setNombre("Carla");
        bibliotecario.setApellido("Soto");
        bibliotecario.setEdad(35);

        // Simulamos la envoltura de HATEOAS agregando un enlace 'self' ficticio
        entityModel = EntityModel.of(bibliotecario, 
                Link.of("http://localhost/api/v2/bibliotecarios/1").withSelfRel());

        // El payload válido modificado (con el fix de la edad)
        request = BibliotecarioRequest.builder()
                .nombre("Carla")
                .apellido("Soto")
                .edad(35)
                .build();
    }

    @Test
    public void testGetAll() throws Exception {
        when(service.getAllEntidad()).thenReturn(List.of(bibliotecario));
        when(assembler.toModel(any(Bibliotecario.class))).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/bibliotecarios")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.bibliotecarioList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.bibliotecarioList[0].nombre").value("Carla"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    public void testGetById() throws Exception {
        when(service.findByIdEntidad(1L)).thenReturn(bibliotecario);
        when(assembler.toModel(any(Bibliotecario.class))).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/bibliotecarios/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Carla"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/v2/bibliotecarios/1"));
    }

    @Test
    public void testCrear() throws Exception {
        when(service.crearEntidad(any())).thenReturn(bibliotecario);
        when(assembler.toModel(any(Bibliotecario.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/bibliotecarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Carla"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    public void testActualizar() throws Exception {
        when(service.actualizarEntidad(any(), any())).thenReturn(bibliotecario);
        when(assembler.toModel(any(Bibliotecario.class))).thenReturn(entityModel);

        mockMvc.perform(put("/api/v2/bibliotecarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carla"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    public void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v2/bibliotecarios/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
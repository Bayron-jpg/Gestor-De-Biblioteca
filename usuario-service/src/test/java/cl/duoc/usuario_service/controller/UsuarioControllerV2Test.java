package cl.duoc.usuario_service.controller;

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

import cl.duoc.usuario_service.assemblers.UsuarioModelAssembler;
import cl.duoc.usuario_service.dto.UsuarioRequest;
import cl.duoc.usuario_service.model.Usuario;
import cl.duoc.usuario_service.service.UsuarioService;

@WebMvcTest(UsuarioControllerV2.class)
@ActiveProfiles("test")
public class UsuarioControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService service;

    @MockitoBean
    private UsuarioModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;
    private UsuarioRequest request;
    private EntityModel<Usuario> entityModel;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Bayron");
        usuario.setApellido("Urrutia");
        usuario.setGmail("bayron221906@gmail.com");
        usuario.setIdTelefono(1L);
        usuario.setIdDireccion(1L);

        request = new UsuarioRequest("Bayron", "Urrutia", "bayron221906@gmail.com", 1L, 1L);

        entityModel = EntityModel.of(usuario);
    }

    @Test
    void testGetAll() throws Exception {
        when(service.getAllEntidad()).thenReturn(List.of(usuario));
        when(assembler.toModel(usuario)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        when(service.findByIdEntidad(1L)).thenReturn(usuario);
        when(assembler.toModel(usuario)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrear() throws Exception {
        when(service.crearEntidad(any(UsuarioRequest.class)))
                .thenReturn(usuario);

        when(assembler.toModel(usuario))
                .thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizar() throws Exception {
        when(service.actualizarEntidad(eq(1L), any(UsuarioRequest.class)))
                .thenReturn(usuario);

        when(assembler.toModel(usuario))
                .thenReturn(entityModel);

        mockMvc.perform(put("/api/v2/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v2/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
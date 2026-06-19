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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import cl.duoc.usuario_service.dto.TelefonoResponse;
import cl.duoc.usuario_service.dto.DireccionResponse;
import cl.duoc.usuario_service.dto.UsuarioRequest;
import cl.duoc.usuario_service.dto.UsuarioResponse;
import cl.duoc.usuario_service.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
@ActiveProfiles("test")
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService service;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioResponse response;
    private UsuarioRequest request;

    @BeforeEach
    void setUp() {
        TelefonoResponse telefono = new TelefonoResponse();
        telefono.setId(1L);
        telefono.setNumero(912345678);
        telefono.setTipo("Celular");

        DireccionResponse direccion = new DireccionResponse();
        direccion.setId(1L);
        direccion.setDireccion("Av. Colon 1234");
        direccion.setComuna("San Bernardo");
        direccion.setRegion("Metropolitana");

        response = new UsuarioResponse();
        response.setId(1L);
        response.setNombre("Bayron");
        response.setApellido("Urrutia");
        response.setGmail("bayron221906@gmail.com");
        response.setTelefono(telefono);
        response.setDireccion(direccion);

        request = new UsuarioRequest("Bayron", "Urrutia", "bayron221906@gmail.com", 1L, 1L);
    }

    @Test
    public void testGetAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Bayron"))
                .andExpect(jsonPath("$[0].gmail").value("bayron221906@gmail.com"));
    }

    @Test
    public void testGetById() throws Exception {
        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Bayron"))
                .andExpect(jsonPath("$.telefono.numero").value(912345678));
    }

    @Test
    public void testCrear() throws Exception {
        when(service.crear(any(UsuarioRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Bayron"))
                .andExpect(jsonPath("$.apellido").value("Urrutia"));
    }

    @Test
    public void testActualizar() throws Exception {
        when(service.actualizar(eq(1L), any(UsuarioRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Bayron"));
    }

    @Test
    public void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }
}
package cl.duoc.usuario_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import cl.duoc.usuario_service.client.TelefonoClient;
import cl.duoc.usuario_service.client.DireccionClient;
import cl.duoc.usuario_service.dto.TelefonoResponse;
import cl.duoc.usuario_service.dto.DireccionResponse;
import cl.duoc.usuario_service.dto.UsuarioRequest;
import cl.duoc.usuario_service.dto.UsuarioResponse;
import cl.duoc.usuario_service.mapper.UsuarioMapper;
import cl.duoc.usuario_service.model.Usuario;
import cl.duoc.usuario_service.repository.UsuarioRepository;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

        @Autowired
        private UsuarioService service;

        @MockitoBean
        private UsuarioRepository repository;

        @MockitoBean
        private UsuarioMapper mapper;

        @MockitoBean
        private TelefonoClient telefonoClient;

        @MockitoBean
        private DireccionClient direccionClient;

        private Usuario crearUsuarioMock(Long id) {
                Usuario usuario = new Usuario();
                usuario.setId(id);
                usuario.setNombre("Bayron");
                usuario.setApellido("Urrutia");
                usuario.setGmail("bayron221906@gmail.com");
                usuario.setIdTelefono(1L);
                usuario.setIdDireccion(1L);
                return usuario;
        }

        private TelefonoResponse crearTelefonoMock() {
                TelefonoResponse telefono = new TelefonoResponse();
                telefono.setId(1L);
                telefono.setNumero(912345678);
                telefono.setTipo("Celular");
                return telefono;
        }

        private DireccionResponse crearDireccionMock() {
                DireccionResponse direccion = new DireccionResponse();
                direccion.setId(1L);
                direccion.setDireccion("Av. Colon 1234");
                direccion.setComuna("San Bernardo");
                direccion.setRegion("Metropolitana");
                return direccion;
        }

        private UsuarioResponse crearUsuarioResponseMock(Long id, TelefonoResponse tel, DireccionResponse dir) {
                UsuarioResponse res = new UsuarioResponse();
                res.setId(id);
                res.setNombre("Bayron");
                res.setApellido("Urrutia");
                res.setGmail("bayron221906@gmail.com");
                res.setTelefono(tel);
                res.setDireccion(dir);
                return res;
        }

        @Test
        public void testGetAll() {
                Usuario usuario = crearUsuarioMock(1L);
                TelefonoResponse telefono = crearTelefonoMock();
                DireccionResponse direccion = crearDireccionMock();
                UsuarioResponse response = crearUsuarioResponseMock(1L, telefono, direccion);

                when(repository.findAll()).thenReturn(List.of(usuario));
                when(telefonoClient.buscarPorId(1L)).thenReturn(telefono);
                when(direccionClient.buscarPorId(1L)).thenReturn(direccion);
                when(mapper.toResponse(usuario, telefono, direccion)).thenReturn(response);

                List<UsuarioResponse> usuarios = service.getAll();
                assertNotNull(usuarios);
                assertEquals(1, usuarios.size());
                assertEquals("Bayron", usuarios.get(0).getNombre());
        }

        @Test
        void testGetAllEntidad() {
                Usuario usuario = crearUsuarioMock(1L);
                when(repository.findAll()).thenReturn(List.of(usuario));

                List<Usuario> resultado = service.getAllEntidad();
                assertEquals(1, resultado.size());
        }

        @Test
        public void testFindById() {
                Long id = 1L;
                Usuario usuario = crearUsuarioMock(id);
                TelefonoResponse telefono = crearTelefonoMock();
                DireccionResponse direccion = crearDireccionMock();
                UsuarioResponse response = crearUsuarioResponseMock(id, telefono, direccion);

                when(repository.findById(id)).thenReturn(Optional.of(usuario));
                when(telefonoClient.buscarPorId(1L)).thenReturn(telefono);
                when(direccionClient.buscarPorId(1L)).thenReturn(direccion);
                when(mapper.toResponse(usuario, telefono, direccion)).thenReturn(response);

                UsuarioResponse encontrado = service.findById(id);
                assertNotNull(encontrado);
                assertEquals(id, encontrado.getId());
                assertEquals("Bayron", encontrado.getNombre());
        }

        @Test
        void testFindById_NotFound() {
                Long id = 99L;
                when(repository.findById(id)).thenReturn(Optional.empty());

                assertThrows(NoSuchElementException.class, () -> service.findById(id));
        }

        @Test
        void testFindByIdEntidad() {
                Long id = 1L;
                Usuario usuario = crearUsuarioMock(id);
                when(repository.findById(id)).thenReturn(Optional.of(usuario));

                Usuario resultado = service.findByIdEntidad(id);
                assertEquals(id, resultado.getId());
        }

        @Test
        void testFindByIdEntidad_NotFound() {
                Long id = 99L;
                when(repository.findById(id)).thenReturn(Optional.empty());

                assertThrows(NoSuchElementException.class, () -> service.findByIdEntidad(id));
        }

        @Test
        public void testCrear() {
                UsuarioRequest request = new UsuarioRequest("Bayron", "Urrutia", "bayron221906@gmail.com", 1L, 1L);

                Usuario usuario = crearUsuarioMock(1L);
                TelefonoResponse telefono = crearTelefonoMock();
                DireccionResponse direccion = crearDireccionMock();
                UsuarioResponse response = crearUsuarioResponseMock(1L, telefono, direccion);

                when(telefonoClient.buscarPorId(1L)).thenReturn(telefono);
                when(direccionClient.buscarPorId(1L)).thenReturn(direccion);
                when(mapper.toEntity(request)).thenReturn(usuario);
                when(repository.save(usuario)).thenReturn(usuario);
                when(mapper.toResponse(usuario, telefono, direccion)).thenReturn(response);

                UsuarioResponse guardado = service.crear(request);

                assertNotNull(guardado);
                assertEquals("Bayron", guardado.getNombre());
        }

        @Test
        void testCrear_TelefonoNoEncontrado() {
                UsuarioRequest request = new UsuarioRequest("Bayron", "Urrutia", "bayron221906@gmail.com", 99L, 1L);
                when(telefonoClient.buscarPorId(99L)).thenReturn(null);

                assertThrows(NoSuchElementException.class, () -> service.crear(request));
        }

        @Test
        void testCrear_DireccionNoEncontrado() {
                UsuarioRequest request = new UsuarioRequest("Bayron", "Urrutia", "bayron221906@gmail.com", 1L, 99L);
                TelefonoResponse telefono = crearTelefonoMock();

                when(telefonoClient.buscarPorId(1L)).thenReturn(telefono);
                when(direccionClient.buscarPorId(99L)).thenReturn(null);

                assertThrows(NoSuchElementException.class, () -> service.crear(request));
        }

        @Test
        void testCrearEntidad() {
                UsuarioRequest request = new UsuarioRequest("Bayron", "Urrutia", "bayron221906@gmail.com", 1L, 1L);
                Usuario usuario = crearUsuarioMock(1L);
                TelefonoResponse telefono = crearTelefonoMock();
                DireccionResponse direccion = crearDireccionMock();

                when(telefonoClient.buscarPorId(1L)).thenReturn(telefono);
                when(direccionClient.buscarPorId(1L)).thenReturn(direccion);
                when(mapper.toEntity(request)).thenReturn(usuario);
                when(repository.save(usuario)).thenReturn(usuario);

                Usuario resultado = service.crearEntidad(request);
                assertEquals("Bayron", resultado.getNombre());
        }

        @Test
        void testCrearEntidad_TelefonoNoEncontrado() {
                UsuarioRequest request = new UsuarioRequest("Bayron", "Urrutia", "bayron@gmail.com", 99L, 1L);

                when(telefonoClient.buscarPorId(99L)).thenReturn(null);

                assertThrows(NoSuchElementException.class, () -> service.crearEntidad(request));
        }

        @Test
        void testCrearEntidad_DireccionNoEncontrado() {
                UsuarioRequest request = new UsuarioRequest("Bayron", "Urrutia", "bayron@gmail.com", 1L, 99L);
                TelefonoResponse telefono = crearTelefonoMock();

                when(telefonoClient.buscarPorId(1L)).thenReturn(telefono);
                when(direccionClient.buscarPorId(99L)).thenReturn(null);

                assertThrows(NoSuchElementException.class, () -> service.crearEntidad(request));
        }

        @Test
        void testActualizar_Exitoso() {
                Long id = 1L;
                Usuario existente = crearUsuarioMock(id);
                UsuarioRequest request = new UsuarioRequest("Bayron Cambiado", "Urrutia", "bayron@gmail.com", 1L, 1L);

                Usuario actualizado = crearUsuarioMock(id);
                actualizado.setNombre("Bayron Cambiado");

                TelefonoResponse telefono = crearTelefonoMock();
                DireccionResponse direccion = crearDireccionMock();

                UsuarioResponse response = crearUsuarioResponseMock(id, telefono, direccion);
                response.setNombre("Bayron Cambiado");

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(telefonoClient.buscarPorId(1L)).thenReturn(telefono);
                when(direccionClient.buscarPorId(1L)).thenReturn(direccion);
                when(repository.save(any(Usuario.class))).thenReturn(actualizado);
                when(mapper.toResponse(actualizado, telefono, direccion)).thenReturn(response);

                UsuarioResponse resultado = service.actualizar(id, request);

                assertNotNull(resultado);
                assertEquals("Bayron Cambiado", resultado.getNombre());
        }

        @Test
        void testActualizar_UsuarioNoEncontrado() {
                Long id = 99L;
                UsuarioRequest request = new UsuarioRequest("Bayron", "Urrutia", "bayron@gmail.com", 1L, 1L);

                when(repository.findById(id)).thenReturn(Optional.empty());

                assertThrows(NoSuchElementException.class, () -> service.actualizar(id, request));
        }

        @Test
        void testActualizar_TelefonoNoEncontrado() {
                Long id = 1L;
                Usuario existente = crearUsuarioMock(id);
                UsuarioRequest request = new UsuarioRequest("Bayron", "Urrutia", "bayron221906@gmail.com", 99L, 1L);

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(telefonoClient.buscarPorId(99L)).thenReturn(null);

                assertThrows(NoSuchElementException.class, () -> service.actualizar(id, request));
        }

        @Test
        void testActualizar_DireccionNoEncontrado() {
                Long id = 1L;
                Usuario existente = crearUsuarioMock(id);
                UsuarioRequest request = new UsuarioRequest("Bayron", "Urrutia", "bayron221906@gmail.com", 1L, 99L);
                TelefonoResponse telefono = crearTelefonoMock();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(telefonoClient.buscarPorId(1L)).thenReturn(telefono);
                when(direccionClient.buscarPorId(99L)).thenReturn(null);

                assertThrows(NoSuchElementException.class, () -> service.actualizar(id, request));
        }

        @Test
        void testActualizarEntidad_Exitoso() {
                Long id = 1L;
                Usuario existente = crearUsuarioMock(id);
                UsuarioRequest request = new UsuarioRequest("Bayron Actualizado", "Urrutia", "bayron@gmail.com", 1L,
                                1L);

                Usuario actualizado = crearUsuarioMock(id);
                actualizado.setNombre("Bayron Actualizado");

                TelefonoResponse telefono = crearTelefonoMock();
                DireccionResponse direccion = crearDireccionMock();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(telefonoClient.buscarPorId(1L)).thenReturn(telefono);
                when(direccionClient.buscarPorId(1L)).thenReturn(direccion);
                when(repository.save(any(Usuario.class))).thenReturn(actualizado);

                Usuario resultado = service.actualizarEntidad(id, request);

                assertNotNull(resultado);
                assertEquals("Bayron Actualizado", resultado.getNombre());
        }

        @Test
        void testActualizarEntidad_UsuarioNoEncontrado() {
                Long id = 99L;
                UsuarioRequest request = new UsuarioRequest("Bayron", "Urrutia", "bayron@gmail.com", 1L, 1L);

                when(repository.findById(id)).thenReturn(Optional.empty());

                assertThrows(NoSuchElementException.class, () -> service.actualizarEntidad(id, request));
        }

        @Test
        void testActualizarEntidad_TelefonoNoEncontrado() {
                Long id = 1L;

                Usuario existente = crearUsuarioMock(id);

                UsuarioRequest request = new UsuarioRequest("Bayron", "Urrutia", "gmail@gmail.com", 99L, 1L);

                when(repository.findById(id))
                                .thenReturn(Optional.of(existente));

                when(telefonoClient.buscarPorId(99L))
                                .thenReturn(null);

                assertThrows(NoSuchElementException.class,
                                () -> service.actualizarEntidad(id, request));
        }

        @Test
        void testActualizarEntidad_DireccionNoEncontrado() {
                Long id = 1L;

                Usuario existente = crearUsuarioMock(id);

                UsuarioRequest request = new UsuarioRequest("Bayron", "Urrutia", "gmail@gmail.com", 1L, 99L);

                when(repository.findById(id))
                                .thenReturn(Optional.of(existente));

                when(telefonoClient.buscarPorId(1L))
                                .thenReturn(crearTelefonoMock());

                when(direccionClient.buscarPorId(99L))
                                .thenReturn(null);

                assertThrows(NoSuchElementException.class,
                                () -> service.actualizarEntidad(id, request));
        }

        @Test
        void testEliminar() {
                Long id = 1L;
                when(repository.existsById(id)).thenReturn(true);
                doNothing().when(repository).deleteById(id);

                service.eliminar(id);
                verify(repository, times(1)).deleteById(id);
        }

        @Test
        void testEliminar_NotFound() {
                Long id = 99L;
                when(repository.existsById(id)).thenReturn(false);

                assertThrows(NoSuchElementException.class, () -> service.eliminar(id));
        }
}
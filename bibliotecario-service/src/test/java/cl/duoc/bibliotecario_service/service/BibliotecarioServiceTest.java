package cl.duoc.bibliotecario_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import cl.duoc.bibliotecario_service.dto.BibliotecarioRequest;
import cl.duoc.bibliotecario_service.dto.BibliotecarioResponse;
import cl.duoc.bibliotecario_service.mapper.BibliotecarioMapper;
import cl.duoc.bibliotecario_service.model.Bibliotecario;
import cl.duoc.bibliotecario_service.repository.BibliotecarioRepository;

@SpringBootTest
@ActiveProfiles("test")
public class BibliotecarioServiceTest {

        @Autowired
        private BibliotecarioService service;

        @MockitoBean
        private BibliotecarioRepository repository;

        @MockitoBean
        private BibliotecarioMapper mapper;

        @Test
        public void testGetAll() {
                Bibliotecario bibliotecario = Bibliotecario.builder()
                                .id(1L)
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                BibliotecarioResponse response = BibliotecarioResponse.builder()
                                .id(1L)
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                when(repository.findAll()).thenReturn(List.of(bibliotecario));
                when(mapper.toResponse(bibliotecario)).thenReturn(response);

                List<BibliotecarioResponse> bibliotecarios = service.getAll();
                assertNotNull(bibliotecarios);
                assertEquals(1, bibliotecarios.size());
                assertEquals("Carla", bibliotecarios.get(0).getNombre());
                assertEquals("Soto", bibliotecarios.get(0).getApellido());
        }

        @Test
        void testGetAllEntidad() {

                Bibliotecario bibliotecario = Bibliotecario.builder()
                                .id(1L)
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                when(repository.findAll()).thenReturn(List.of(bibliotecario));

                List<Bibliotecario> resultado = service.getAllEntidad();

                assertEquals(1, resultado.size());
        }

        @Test
        public void testFindById() {
                Long id = 1L;
                Bibliotecario bibliotecario = Bibliotecario.builder()
                                .id(1L)
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                BibliotecarioResponse response = BibliotecarioResponse.builder()
                                .id(1L)
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(bibliotecario));
                when(mapper.toResponse(bibliotecario)).thenReturn(response);

                BibliotecarioResponse encontrado = service.findById(id);
                assertNotNull(encontrado);
                assertEquals(id, encontrado.getId());
                assertEquals("Carla", encontrado.getNombre());
        }

        @Test
        void testFindById_NotFound() {
                Long id = 99L;

                when(repository.findById(id))
                                .thenReturn(Optional.empty());

                assertThrows(NoSuchElementException.class,
                                () -> service.findById(id));
        }

        @Test
        void testFindByIdEntidad() {

                Long id = 1L;

                Bibliotecario bibliotecario = Bibliotecario.builder()
                                .id(id)
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(bibliotecario));

                Bibliotecario resultado = service.findByIdEntidad(id);

                assertEquals(id, resultado.getId());
        }

        @Test
        void testFindByIdEntidad_NotFound() {

                Long id = 99L;

                when(repository.findById(id))
                                .thenReturn(Optional.empty());

                assertThrows(NoSuchElementException.class,
                                () -> service.findByIdEntidad(id));
        }

        @Test
        public void testCrear() {
                BibliotecarioRequest request = BibliotecarioRequest.builder()
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                Bibliotecario bibliotecario = Bibliotecario.builder()
                                .id(1L)
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                BibliotecarioResponse response = BibliotecarioResponse.builder()
                                .id(1L)
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                when(mapper.toEntity(request)).thenReturn(bibliotecario);
                when(repository.save(bibliotecario)).thenReturn(bibliotecario);
                when(mapper.toResponse(bibliotecario)).thenReturn(response);

                BibliotecarioResponse guardado = service.crear(request);

                assertNotNull(guardado);
                assertEquals("Carla", guardado.getNombre());
        }

        @Test
        void testCrearEntidad() {

                BibliotecarioRequest request = BibliotecarioRequest.builder()
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                Bibliotecario bibliotecario = Bibliotecario.builder()
                                .id(1L)
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                when(mapper.toEntity(request)).thenReturn(bibliotecario);
                when(repository.save(bibliotecario)).thenReturn(bibliotecario);

                Bibliotecario resultado = service.crearEntidad(request);

                assertEquals("Carla", resultado.getNombre());
        }

        @Test
        void testActualizar() {
                Long id = 1L;

                Bibliotecario existente = Bibliotecario.builder()
                                .id(id)
                                .nombre("Viejo")
                                .apellido("Apellido")
                                .edad(40)
                                .build();

                BibliotecarioRequest request = BibliotecarioRequest.builder()
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                Bibliotecario actualizado = Bibliotecario.builder()
                                .id(id)
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                BibliotecarioResponse response = BibliotecarioResponse.builder()
                                .id(id)
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(repository.save(existente)).thenReturn(actualizado);
                when(mapper.toResponse(actualizado)).thenReturn(response);

                BibliotecarioResponse resultado = service.actualizar(id, request);

                assertEquals("Carla", resultado.getNombre());
        }

        @Test
        void testActualizar_NotFound() {
                Long id = 99L;

                BibliotecarioRequest request = BibliotecarioRequest.builder()
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                when(repository.findById(id)).thenReturn(Optional.empty());

                assertThrows(NoSuchElementException.class,
                                () -> service.actualizar(id, request));
        }

        @Test
        void testActualizarEntidad() {

                Long id = 1L;

                Bibliotecario existente = Bibliotecario.builder()
                                .id(id)
                                .nombre("Viejo")
                                .apellido("Apellido")
                                .edad(40)
                                .build();

                BibliotecarioRequest request = BibliotecarioRequest.builder()
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                Bibliotecario actualizado = Bibliotecario.builder()
                                .id(id)
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(repository.save(existente)).thenReturn(actualizado);

                Bibliotecario resultado = service.actualizarEntidad(id, request);

                assertEquals("Carla", resultado.getNombre());
        }

        @Test
        void testActualizarEntidad_NotFound() {

                Long id = 99L;

                BibliotecarioRequest request = BibliotecarioRequest.builder()
                                .nombre("Carla")
                                .apellido("Soto")
                                .edad(35)
                                .build();

                when(repository.findById(id)).thenReturn(Optional.empty());

                assertThrows(NoSuchElementException.class,
                                () -> service.actualizarEntidad(id, request));
        }

        @Test
        public void testEliminar() {
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

                assertThrows(NoSuchElementException.class,
                                () -> service.eliminar(id));
        }
}
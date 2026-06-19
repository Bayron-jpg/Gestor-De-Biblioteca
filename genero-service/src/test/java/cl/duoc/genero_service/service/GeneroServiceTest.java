package cl.duoc.genero_service.service;

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

import cl.duoc.genero_service.dto.GeneroRequest;
import cl.duoc.genero_service.dto.GeneroResponse;
import cl.duoc.genero_service.mapper.GeneroMapper;
import cl.duoc.genero_service.model.Genero;
import cl.duoc.genero_service.repository.GeneroRepository;

@SpringBootTest
@ActiveProfiles("test")
public class GeneroServiceTest {

        @Autowired
        private GeneroService service;

        @MockitoBean
        private GeneroRepository repository;

        @MockitoBean
        private GeneroMapper mapper;

        @Test
        public void testGetAll() {
                Genero genero = Genero.builder()
                                .id(1L)
                                .nombre("Fantasía")
                                .descripcion("Historias con elementos mágicos o sobrenaturales.")
                                .build();

                GeneroResponse response = GeneroResponse.builder()
                                .id(1L)
                                .nombre("Fantasía")
                                .descripcion("Historias con elementos mágicos o sobrenaturales.")
                                .build();

                when(repository.findAll()).thenReturn(List.of(genero));
                when(mapper.toResponse(genero)).thenReturn(response);

                List<GeneroResponse> generos = service.getAll();
                assertNotNull(generos);
                assertEquals(1, generos.size());
                assertEquals("Fantasía", generos.get(0).getNombre());
                assertEquals("Historias con elementos mágicos o sobrenaturales.", generos.get(0).getDescripcion());
        }

        @Test
        void testGetAllEntidad() {

                Genero genero = Genero.builder()
                                .id(1L)
                                .nombre("Fantasía")
                                .descripcion("Historias con elementos mágicos.")
                                .build();

                when(repository.findAll()).thenReturn(List.of(genero));

                List<Genero> resultado = service.getAllEntidad();

                assertEquals(1, resultado.size());
        }

        @Test
        public void testFindById() {
                Long id = 1L;
                Genero genero = Genero.builder()
                                .id(1L)
                                .nombre("Fantasía")
                                .descripcion("Historias con elementos mágicos o sobrenaturales.")
                                .build();

                GeneroResponse response = GeneroResponse.builder()
                                .id(1L)
                                .nombre("Fantasía")
                                .descripcion("Historias con elementos mágicos o sobrenaturales.")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(genero));
                when(mapper.toResponse(genero)).thenReturn(response);

                GeneroResponse encontrado = service.findById(id);
                assertNotNull(encontrado);
                assertEquals(id, encontrado.getId());
                assertEquals("Fantasía", encontrado.getNombre());
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

                Genero genero = Genero.builder()
                                .id(id)
                                .nombre("Fantasía")
                                .descripcion("Historias con elementos mágicos.")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(genero));

                Genero resultado = service.findByIdEntidad(id);

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
                GeneroRequest request = GeneroRequest.builder()
                                .nombre("Fantasía")
                                .descripcion("Historias con elementos mágicos o sobrenaturales.")
                                .build();

                Genero genero = Genero.builder()
                                .id(1L)
                                .nombre("Fantasía")
                                .descripcion("Historias con elementos mágicos o sobrenaturales.")
                                .build();

                GeneroResponse response = GeneroResponse.builder()
                                .id(1L)
                                .nombre("Fantasía")
                                .descripcion("Historias con elementos mágicos o sobrenaturales.")
                                .build();

                when(mapper.toEntity(request)).thenReturn(genero);
                when(repository.save(genero)).thenReturn(genero);
                when(mapper.toResponse(genero)).thenReturn(response);

                GeneroResponse guardado = service.crear(request);

                assertNotNull(guardado);
                assertEquals("Fantasía", guardado.getNombre());
        }

        @Test
        void testCrearEntidad() {

                GeneroRequest request = GeneroRequest.builder()
                                .nombre("Fantasía")
                                .descripcion("Historias con elementos mágicos.")
                                .build();

                Genero genero = Genero.builder()
                                .id(1L)
                                .nombre("Fantasía")
                                .descripcion("Historias con elementos mágicos.")
                                .build();

                when(mapper.toEntity(request)).thenReturn(genero);
                when(repository.save(genero)).thenReturn(genero);

                Genero resultado = service.crearEntidad(request);

                assertEquals("Fantasía", resultado.getNombre());
        }

        @Test
        void testActualizar() {
                Long id = 1L;

                Genero existente = Genero.builder()
                                .id(id)
                                .nombre("Viejo")
                                .descripcion("Descripción vieja.")
                                .build();

                GeneroRequest request = GeneroRequest.builder()
                                .nombre("Nuevo")
                                .descripcion("Descripción nueva.")
                                .build();

                Genero actualizado = Genero.builder()
                                .id(id)
                                .nombre("Nuevo")
                                .descripcion("Descripción nueva.")
                                .build();

                GeneroResponse response = GeneroResponse.builder()
                                .id(id)
                                .nombre("Nuevo")
                                .descripcion("Descripción nueva.")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(repository.save(existente)).thenReturn(actualizado);
                when(mapper.toResponse(actualizado)).thenReturn(response);

                GeneroResponse resultado = service.actualizar(id, request);

                assertEquals("Nuevo", resultado.getNombre());
        }

        @Test
        void testActualizar_NotFound() {
                Long id = 99L;

                GeneroRequest request = GeneroRequest.builder()
                                .nombre("Nuevo")
                                .descripcion("Descripción nueva.")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.empty());

                assertThrows(NoSuchElementException.class,
                                () -> service.actualizar(id, request));
        }

        @Test
        void testActualizarEntidad() {

                Long id = 1L;

                Genero existente = Genero.builder()
                                .id(id)
                                .nombre("Viejo")
                                .descripcion("Descripción vieja.")
                                .build();

                GeneroRequest request = GeneroRequest.builder()
                                .nombre("Nuevo")
                                .descripcion("Descripción nueva.")
                                .build();

                Genero actualizado = Genero.builder()
                                .id(id)
                                .nombre("Nuevo")
                                .descripcion("Descripción nueva.")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(repository.save(existente)).thenReturn(actualizado);

                Genero resultado = service.actualizarEntidad(id, request);

                assertEquals("Nuevo", resultado.getNombre());
        }

        @Test
        void testActualizarEntidad_NotFound() {

                Long id = 99L;

                GeneroRequest request = GeneroRequest.builder()
                                .nombre("Nuevo")
                                .descripcion("Descripción nueva.")
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
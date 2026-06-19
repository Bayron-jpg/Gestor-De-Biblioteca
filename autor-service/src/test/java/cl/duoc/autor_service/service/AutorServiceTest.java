package cl.duoc.autor_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import cl.duoc.autor_service.dto.AutorRequest;
import cl.duoc.autor_service.dto.AutorResponse;
import cl.duoc.autor_service.mapper.AutorMapper;
import cl.duoc.autor_service.model.Autor;
import cl.duoc.autor_service.repository.AutorRepository;

@SpringBootTest
@ActiveProfiles("test")
public class AutorServiceTest {

        @Autowired
        private AutorService service;

        @MockitoBean
        private AutorRepository repository;

        @MockitoBean
        private AutorMapper mapper;

        @Test
        public void testGetAll() {
                Autor autor = Autor.builder()
                                .id(1L)
                                .nombre("Claudio Irarrazabal")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                AutorResponse response = AutorResponse.builder()
                                .id(1L)
                                .nombre("Claudio Irarrazabal")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                when(repository.findAll()).thenReturn(List.of(autor));
                when(mapper.toResponse(autor)).thenReturn(response);

                List<AutorResponse> autores = service.getAll();
                assertNotNull(autores);
                assertEquals(1, autores.size());
                assertEquals("Claudio Irarrazabal", autores.get(0).getNombre());
                assertEquals(LocalDate.of(2001, 1, 1), autores.get(0).getFechaNacimiento());
        }

        @Test
        void testGetAllEntidad() {

                Autor autor = Autor.builder()
                                .id(1L)
                                .nombre("Claudio")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                when(repository.findAll()).thenReturn(List.of(autor));

                List<Autor> resultado = service.getAllEntidad();

                assertEquals(1, resultado.size());
        }

        @Test
        public void testFindById() {
                Long id = 1L;
                Autor autor = Autor.builder()
                                .id(1L)
                                .nombre("Claudio Irarrazabal")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                AutorResponse response = AutorResponse.builder()
                                .id(1L)
                                .nombre("Claudio Irarrazabal")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(autor));
                when(mapper.toResponse(autor)).thenReturn(response);

                AutorResponse encontrado = service.findById(id);
                assertNotNull(encontrado);
                assertEquals(id, encontrado.getId());
                assertEquals("Claudio Irarrazabal", encontrado.getNombre());
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

                Autor autor = Autor.builder()
                                .id(id)
                                .nombre("Claudio")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(autor));

                Autor resultado = service.findByIdEntidad(id);

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
                AutorRequest request = AutorRequest.builder()
                                .nombre("Claudio Irarrazabal")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                Autor autor = Autor.builder()
                                .id(1L)
                                .nombre("Claudio Irarrazabal")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                AutorResponse response = AutorResponse.builder()
                                .id(1L)
                                .nombre("Claudio Irarrazabal")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                when(mapper.toEntity(request)).thenReturn(autor);
                when(repository.save(autor)).thenReturn(autor);
                when(mapper.toResponse(autor)).thenReturn(response);

                AutorResponse guardado = service.crear(request);

                assertNotNull(guardado);
                assertEquals("Claudio Irarrazabal", guardado.getNombre());
        }

        @Test
        void testCrearEntidad() {

                AutorRequest request = AutorRequest.builder()
                                .nombre("Claudio")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                Autor autor = Autor.builder()
                                .id(1L)
                                .nombre("Claudio")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                when(mapper.toEntity(request)).thenReturn(autor);
                when(repository.save(autor)).thenReturn(autor);

                Autor resultado = service.crearEntidad(request);

                assertEquals("Claudio", resultado.getNombre());
        }

        @Test
        void testActualizar() {
                Long id = 1L;

                Autor existente = Autor.builder()
                                .id(id)
                                .nombre("Viejo")
                                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                                .build();

                AutorRequest request = AutorRequest.builder()
                                .nombre("Nuevo")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                Autor actualizado = Autor.builder()
                                .id(id)
                                .nombre("Nuevo")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                AutorResponse response = AutorResponse.builder()
                                .id(id)
                                .nombre("Nuevo")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(repository.save(existente)).thenReturn(actualizado);
                when(mapper.toResponse(actualizado)).thenReturn(response);

                AutorResponse resultado = service.actualizar(id, request);

                assertEquals("Nuevo", resultado.getNombre());
        }

        @Test
        void testActualizar_NotFound() {
                Long id = 99L;

                AutorRequest request = AutorRequest.builder()
                                .nombre("Nuevo")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                when(repository.findById(id)).thenReturn(Optional.empty());

                assertThrows(NoSuchElementException.class,
                                () -> service.actualizar(id, request));
        }

        @Test
        void testActualizarEntidad() {

                Long id = 1L;

                Autor existente = Autor.builder()
                                .id(id)
                                .nombre("Viejo")
                                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                                .build();

                AutorRequest request = AutorRequest.builder()
                                .nombre("Nuevo")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                Autor actualizado = Autor.builder()
                                .id(id)
                                .nombre("Nuevo")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(repository.save(existente)).thenReturn(actualizado);

                Autor resultado = service.actualizarEntidad(id, request);

                assertEquals("Nuevo", resultado.getNombre());
        }

        @Test
        void testActualizarEntidad_NotFound() {

                Long id = 99L;

                AutorRequest request = AutorRequest.builder()
                                .nombre("Nuevo")
                                .fechaNacimiento(LocalDate.of(2001, 1, 1))
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
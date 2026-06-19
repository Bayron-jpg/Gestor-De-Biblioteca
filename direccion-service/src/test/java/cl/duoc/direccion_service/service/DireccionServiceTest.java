package cl.duoc.direccion_service.service;

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

import cl.duoc.direccion_service.dto.DireccionRequest;
import cl.duoc.direccion_service.dto.DireccionResponse;
import cl.duoc.direccion_service.mapper.DireccionMapper;
import cl.duoc.direccion_service.model.Direccion;
import cl.duoc.direccion_service.repository.DireccionRepository;

@SpringBootTest
@ActiveProfiles("test")
public class DireccionServiceTest {

        @Autowired
        private DireccionService service;

        @MockitoBean
        private DireccionRepository repository;

        @MockitoBean
        private DireccionMapper mapper;

        @Test
        public void testGetAll() {
                Direccion direccion = Direccion.builder()
                                .id(1L)
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                DireccionResponse response = DireccionResponse.builder()
                                .id(1L)
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                when(repository.findAll()).thenReturn(List.of(direccion));
                when(mapper.toResponse(direccion)).thenReturn(response);

                List<DireccionResponse> direcciones = service.getAll();
                assertNotNull(direcciones);
                assertEquals(1, direcciones.size());
                assertEquals("Av. Providencia 123", direcciones.get(0).getDireccion());
                assertEquals("Providencia", direcciones.get(0).getComuna());
        }

        @Test
        void testGetAllEntidad() {
                Direccion direccion = Direccion.builder()
                                .id(1L)
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                when(repository.findAll()).thenReturn(List.of(direccion));

                List<Direccion> resultado = service.getAllEntidad();

                assertEquals(1, resultado.size());
        }

        @Test
        public void testFindById() {
                Long id = 1L;
                Direccion direccion = Direccion.builder()
                                .id(1L)
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                DireccionResponse response = DireccionResponse.builder()
                                .id(1L)
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(direccion));
                when(mapper.toResponse(direccion)).thenReturn(response);

                DireccionResponse encontrada = service.findById(id);
                assertNotNull(encontrada);
                assertEquals(id, encontrada.getId());
                assertEquals("Av. Providencia 123", encontrada.getDireccion());
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

                Direccion direccion = Direccion.builder()
                                .id(id)
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(direccion));

                Direccion resultado = service.findByIdEntidad(id);

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
                DireccionRequest request = DireccionRequest.builder()
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                Direccion direccion = Direccion.builder()
                                .id(1L)
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                DireccionResponse response = DireccionResponse.builder()
                                .id(1L)
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                when(mapper.toEntity(request)).thenReturn(direccion);
                when(repository.save(direccion)).thenReturn(direccion);
                when(mapper.toResponse(direccion)).thenReturn(response);

                DireccionResponse guardada = service.crear(request);

                assertNotNull(guardada);
                assertEquals("Av. Providencia 123", guardada.getDireccion());
        }

        @Test
        void testCrearEntidad() {
                DireccionRequest request = DireccionRequest.builder()
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                Direccion direccion = Direccion.builder()
                                .id(1L)
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                when(mapper.toEntity(request)).thenReturn(direccion);
                when(repository.save(direccion)).thenReturn(direccion);

                Direccion resultado = service.crearEntidad(request);

                assertEquals("Av. Providencia 123", resultado.getDireccion());
        }

        @Test
        void testActualizar() {
                Long id = 1L;

                Direccion existente = Direccion.builder()
                                .id(id)
                                .direccion("Calle Vieja 999")
                                .comuna("Santiago")
                                .region("Metropolitana")
                                .build();

                DireccionRequest request = DireccionRequest.builder()
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                Direccion actualizada = Direccion.builder()
                                .id(id)
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                DireccionResponse response = DireccionResponse.builder()
                                .id(id)
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(repository.save(existente)).thenReturn(actualizada);
                when(mapper.toResponse(actualizada)).thenReturn(response);

                DireccionResponse resultado = service.actualizar(id, request);

                assertEquals("Av. Providencia 123", resultado.getDireccion());
        }

        @Test
        void testActualizar_NotFound() {
                Long id = 99L;

                DireccionRequest request = DireccionRequest.builder()
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.empty());

                assertThrows(NoSuchElementException.class,
                                () -> service.actualizar(id, request));
        }

        @Test
        void testActualizarEntidad() {
                Long id = 1L;

                Direccion existente = Direccion.builder()
                                .id(id)
                                .direccion("Calle Vieja 999")
                                .comuna("Santiago")
                                .region("Metropolitana")
                                .build();

                DireccionRequest request = DireccionRequest.builder()
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                Direccion actualizada = Direccion.builder()
                                .id(id)
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(repository.save(existente)).thenReturn(actualizada);

                Direccion resultado = service.actualizarEntidad(id, request);

                assertEquals("Av. Providencia 123", resultado.getDireccion());
        }

        @Test
        void testActualizarEntidad_NotFound() {
                Long id = 99L;

                DireccionRequest request = DireccionRequest.builder()
                                .direccion("Av. Providencia 123")
                                .comuna("Providencia")
                                .region("Metropolitana")
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
package cl.duoc.telefono_service.service;

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

import cl.duoc.telefono_service.dto.TelefonoRequest;
import cl.duoc.telefono_service.dto.TelefonoResponse;
import cl.duoc.telefono_service.mapper.TelefonoMapper;
import cl.duoc.telefono_service.model.Telefono;
import cl.duoc.telefono_service.repository.TelefonoRepository;

@SpringBootTest
@ActiveProfiles("test")
public class TelefonoServiceTest {

        @Autowired
        private TelefonoService service;

        @MockitoBean
        private TelefonoRepository repository;

        @MockitoBean
        private TelefonoMapper mapper;

        @Test
        public void testGetAll() {
                Telefono telefono = Telefono.builder()
                                .id(1L)
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                TelefonoResponse response = TelefonoResponse.builder()
                                .id(1L)
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                when(repository.findAll()).thenReturn(List.of(telefono));
                when(mapper.toResponse(telefono)).thenReturn(response);

                List<TelefonoResponse> telefonos = service.getAll();
                assertNotNull(telefonos);
                assertEquals(1, telefonos.size());
                assertEquals(987654321, telefonos.get(0).getNumero());
                assertEquals("Móvil", telefonos.get(0).getTipo());
        }

        @Test
        void testGetAllEntidad() {
                Telefono telefono = Telefono.builder()
                                .id(1L)
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                when(repository.findAll()).thenReturn(List.of(telefono));

                List<Telefono> resultado = service.getAllEntidad();

                assertEquals(1, resultado.size());
        }

        @Test
        public void testFindById() {
                Long id = 1L;
                Telefono telefono = Telefono.builder()
                                .id(1L)
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                TelefonoResponse response = TelefonoResponse.builder()
                                .id(1L)
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(telefono));
                when(mapper.toResponse(telefono)).thenReturn(response);

                TelefonoResponse encontrado = service.findById(id);
                assertNotNull(encontrado);
                assertEquals(id, encontrado.getId());
                assertEquals(987654321, encontrado.getNumero());
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

                Telefono telefono = Telefono.builder()
                                .id(id)
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(telefono));

                Telefono resultado = service.findByIdEntidad(id);

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
                TelefonoRequest request = TelefonoRequest.builder()
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                Telefono telefono = Telefono.builder()
                                .id(1L)
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                TelefonoResponse response = TelefonoResponse.builder()
                                .id(1L)
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                when(mapper.toEntity(request)).thenReturn(telefono);
                when(repository.save(telefono)).thenReturn(telefono);
                when(mapper.toResponse(telefono)).thenReturn(response);

                TelefonoResponse guardado = service.crear(request);

                assertNotNull(guardado);
                assertEquals(987654321, guardado.getNumero());
        }

        @Test
        void testCrearEntidad() {
                TelefonoRequest request = TelefonoRequest.builder()
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                Telefono telefono = Telefono.builder()
                                .id(1L)
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                when(mapper.toEntity(request)).thenReturn(telefono);
                when(repository.save(telefono)).thenReturn(telefono);

                Telefono resultado = service.crearEntidad(request);

                assertEquals(987654321, resultado.getNumero());
        }

        @Test
        void testActualizar() {
                Long id = 1L;

                Telefono existente = Telefono.builder()
                                .id(id)
                                .numero(111111111)
                                .tipo("Fijo")
                                .build();

                TelefonoRequest request = TelefonoRequest.builder()
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                Telefono actualizado = Telefono.builder()
                                .id(id)
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                TelefonoResponse response = TelefonoResponse.builder()
                                .id(id)
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(repository.save(existente)).thenReturn(actualizado);
                when(mapper.toResponse(actualizado)).thenReturn(response);

                TelefonoResponse resultado = service.actualizar(id, request);

                assertEquals(987654321, resultado.getNumero());
        }

        @Test
        void testActualizar_NotFound() {
                Long id = 99L;

                TelefonoRequest request = TelefonoRequest.builder()
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.empty());

                assertThrows(NoSuchElementException.class,
                                () -> service.actualizar(id, request));
        }

        @Test
        void testActualizarEntidad() {
                Long id = 1L;

                Telefono existente = Telefono.builder()
                                .id(id)
                                .numero(111111111)
                                .tipo("Fijo")
                                .build();

                TelefonoRequest request = TelefonoRequest.builder()
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                Telefono actualizado = Telefono.builder()
                                .id(id)
                                .numero(987654321)
                                .tipo("Móvil")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(repository.save(existente)).thenReturn(actualizado);

                Telefono resultado = service.actualizarEntidad(id, request);

                assertEquals(987654321, resultado.getNumero());
        }

        @Test
        void testActualizarEntidad_NotFound() {
                Long id = 99L;

                TelefonoRequest request = TelefonoRequest.builder()
                                .numero(987654321)
                                .tipo("Móvil")
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
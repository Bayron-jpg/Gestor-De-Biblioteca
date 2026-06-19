package cl.duoc.piso_service.service;

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

import cl.duoc.piso_service.dto.PisoRequest;
import cl.duoc.piso_service.dto.PisoResponse;
import cl.duoc.piso_service.mapper.PisoMapper;
import cl.duoc.piso_service.model.Piso;
import cl.duoc.piso_service.repository.PisoRepository;

@SpringBootTest
@ActiveProfiles("test")
public class PisoServiceTest {

        @Autowired
        private PisoService service;

        @MockitoBean
        private PisoRepository repository;

        @MockitoBean
        private PisoMapper mapper;

        @Test
        public void testGetAll() {
                Piso piso = Piso.builder()
                                .id(1L)
                                .numero("1")
                                .descripcion("Piso de administración.")
                                .build();

                PisoResponse response = PisoResponse.builder()
                                .id(1L)
                                .numero("1")
                                .descripcion("Piso de administración.")
                                .build();

                when(repository.findAll()).thenReturn(List.of(piso));
                when(mapper.toResponse(piso)).thenReturn(response);

                List<PisoResponse> pisos = service.getAll();
                assertNotNull(pisos);
                assertEquals(1, pisos.size());
                assertEquals("1", pisos.get(0).getNumero());
                assertEquals("Piso de administración.", pisos.get(0).getDescripcion());
        }

        @Test
        void testGetAllEntidad() {
                Piso piso = Piso.builder()
                                .id(1L)
                                .numero("1")
                                .descripcion("Piso de administración.")
                                .build();

                when(repository.findAll()).thenReturn(List.of(piso));

                List<Piso> resultado = service.getAllEntidad();

                assertEquals(1, resultado.size());
        }

        @Test
        public void testFindById() {
                Long id = 1L;
                Piso piso = Piso.builder()
                                .id(1L)
                                .numero("1")
                                .descripcion("Piso de administración.")
                                .build();

                PisoResponse response = PisoResponse.builder()
                                .id(1L)
                                .numero("1")
                                .descripcion("Piso de administración.")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(piso));
                when(mapper.toResponse(piso)).thenReturn(response);

                PisoResponse encontrado = service.findById(id);
                assertNotNull(encontrado);
                assertEquals(id, encontrado.getId());
                assertEquals("1", encontrado.getNumero());
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

                Piso piso = Piso.builder()
                                .id(id)
                                .numero("1")
                                .descripcion("Piso de administración.")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(piso));

                Piso resultado = service.findByIdEntidad(id);

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
                PisoRequest request = PisoRequest.builder()
                                .numero("1")
                                .descripcion("Piso de administración.")
                                .build();

                Piso piso = Piso.builder()
                                .id(1L)
                                .numero("1")
                                .descripcion("Piso de administración.")
                                .build();

                PisoResponse response = PisoResponse.builder()
                                .id(1L)
                                .numero("1")
                                .descripcion("Piso de administración.")
                                .build();

                when(mapper.toEntity(request)).thenReturn(piso);
                when(repository.save(piso)).thenReturn(piso);
                when(mapper.toResponse(piso)).thenReturn(response);

                PisoResponse guardado = service.crear(request);

                assertNotNull(guardado);
                assertEquals("1", guardado.getNumero());
        }

        @Test
        void testCrearEntidad() {
                PisoRequest request = PisoRequest.builder()
                                .numero("1")
                                .descripcion("Piso de administración.")
                                .build();

                Piso piso = Piso.builder()
                                .id(1L)
                                .numero("1")
                                .descripcion("Piso de administración.")
                                .build();

                when(mapper.toEntity(request)).thenReturn(piso);
                when(repository.save(piso)).thenReturn(piso);

                Piso resultado = service.crearEntidad(request);

                assertEquals("1", resultado.getNumero());
        }

        @Test
        void testActualizar() {
                Long id = 1L;

                Piso existente = Piso.builder()
                                .id(id)
                                .numero("1")
                                .descripcion("Descripción vieja.")
                                .build();

                PisoRequest request = PisoRequest.builder()
                                .numero("2")
                                .descripcion("Piso de administración.")
                                .build();

                Piso actualizado = Piso.builder()
                                .id(id)
                                .numero("2")
                                .descripcion("Piso de administración.")
                                .build();

                PisoResponse response = PisoResponse.builder()
                                .id(id)
                                .numero("2")
                                .descripcion("Piso de administración.")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(repository.save(existente)).thenReturn(actualizado);
                when(mapper.toResponse(actualizado)).thenReturn(response);

                PisoResponse resultado = service.actualizar(id, request);

                assertEquals("2", resultado.getNumero());
        }

        @Test
        void testActualizar_NotFound() {
                Long id = 99L;

                PisoRequest request = PisoRequest.builder()
                                .numero("2")
                                .descripcion("Piso de administración.")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.empty());

                assertThrows(NoSuchElementException.class,
                                () -> service.actualizar(id, request));
        }

        @Test
        void testActualizarEntidad() {
                Long id = 1L;

                Piso existente = Piso.builder()
                                .id(id)
                                .numero("1")
                                .descripcion("Descripción vieja.")
                                .build();

                PisoRequest request = PisoRequest.builder()
                                .numero("2")
                                .descripcion("Piso de administración.")
                                .build();

                Piso actualizado = Piso.builder()
                                .id(id)
                                .numero("2")
                                .descripcion("Piso de administración.")
                                .build();

                when(repository.findById(id)).thenReturn(Optional.of(existente));
                when(repository.save(existente)).thenReturn(actualizado);

                Piso resultado = service.actualizarEntidad(id, request);

                assertEquals("2", resultado.getNumero());
        }

        @Test
        void testActualizarEntidad_NotFound() {
                Long id = 99L;

                PisoRequest request = PisoRequest.builder()
                                .numero("2")
                                .descripcion("Piso de administración.")
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
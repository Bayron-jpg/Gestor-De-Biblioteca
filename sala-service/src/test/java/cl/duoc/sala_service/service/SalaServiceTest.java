package cl.duoc.sala_service.service;

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

import cl.duoc.sala_service.client.PisoClient;
import cl.duoc.sala_service.dto.PisoResponse;
import cl.duoc.sala_service.dto.SalaRequest;
import cl.duoc.sala_service.dto.SalaResponse;
import cl.duoc.sala_service.mapper.SalaMapper;
import cl.duoc.sala_service.model.Sala;
import cl.duoc.sala_service.repository.SalaRepository;

@SpringBootTest
@ActiveProfiles("test")
public class SalaServiceTest {

    @Autowired
    private SalaService service;

    @MockitoBean
    private SalaRepository repository;

    @MockitoBean
    private SalaMapper mapper;

    @MockitoBean
    private PisoClient pisoClient;

    @Test
    public void testGetAll() {
        Sala sala = Sala.builder()
                .id(1L)
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();

        PisoResponse piso = new PisoResponse(1L, "1", "Primer piso");

        SalaResponse response = SalaResponse.builder()
                .id(1L)
                .nombre("Sala A101")
                .capacidad(30)
                .piso(piso)
                .build();

        when(repository.findAll()).thenReturn(List.of(sala));
        when(pisoClient.buscarPorId(1L)).thenReturn(piso);
        when(mapper.toResponse(sala, piso)).thenReturn(response);

        List<SalaResponse> salas = service.getAll();
        assertNotNull(salas);
        assertEquals(1, salas.size());
        assertEquals("Sala A101", salas.get(0).getNombre());
    }

    @Test
    void testGetAllEntidad() {
        Sala sala = Sala.builder()
                .id(1L)
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();

        when(repository.findAll()).thenReturn(List.of(sala));

        List<Sala> resultado = service.getAllEntidad();

        assertEquals(1, resultado.size());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Sala sala = Sala.builder()
                .id(1L)
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();

        PisoResponse piso = new PisoResponse(1L, "1", "Primer piso");

        SalaResponse response = SalaResponse.builder()
                .id(1L)
                .nombre("Sala A101")
                .capacidad(30)
                .piso(piso)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(sala));
        when(pisoClient.buscarPorId(1L)).thenReturn(piso);
        when(mapper.toResponse(sala, piso)).thenReturn(response);

        SalaResponse encontrado = service.findById(id);
        assertNotNull(encontrado);
        assertEquals(id, encontrado.getId());
        assertEquals("Sala A101", encontrado.getNombre());
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
        Sala sala = Sala.builder()
                .id(id)
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(sala));

        Sala resultado = service.findByIdEntidad(id);

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
        SalaRequest request = SalaRequest.builder()
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();
        Sala sala = Sala.builder()
                .id(1L)
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();

        PisoResponse piso = new PisoResponse(1L, "1", "Primer piso");

        SalaResponse response = SalaResponse.builder()
                .id(1L)
                .nombre("Sala A101")
                .capacidad(30)
                .piso(piso)
                .build();

        when(pisoClient.buscarPorId(1L)).thenReturn(piso);
        when(mapper.toEntity(request)).thenReturn(sala);
        when(repository.save(sala)).thenReturn(sala);
        when(mapper.toResponse(sala, piso)).thenReturn(response);

        SalaResponse guardado = service.crear(request);

        assertNotNull(guardado);
        assertEquals("Sala A101", guardado.getNombre());
    }

    @Test
    void testCrear_PisoNoEncontrado() {
        SalaRequest request = SalaRequest.builder()
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(99L)
                .build();

        when(pisoClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.crear(request));
    }

    @Test
    void testCrearEntidad() {
        SalaRequest request = SalaRequest.builder()
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();
        Sala sala = Sala.builder()
                .id(1L)
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();

        PisoResponse piso = new PisoResponse(1L, "1", "Primer piso");

        when(pisoClient.buscarPorId(1L)).thenReturn(piso);
        when(mapper.toEntity(request)).thenReturn(sala);
        when(repository.save(sala)).thenReturn(sala);

        Sala resultado = service.crearEntidad(request);

        assertEquals("Sala A101", resultado.getNombre());
    }

    @Test
    void testCrearEntidad_PisoNoEncontrado() {
        SalaRequest request = SalaRequest.builder()
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(99L)
                .build();

        when(pisoClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.crearEntidad(request));
    }

    @Test
    void testActualizar() {
        Long id = 1L;
        Sala existente = Sala.builder()
                .id(id)
                .nombre("Sala Vieja")
                .capacidad(10)
                .idPiso(1L)
                .build();
        SalaRequest request = SalaRequest.builder()
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();
        Sala actualizado = Sala.builder()
                .id(id)
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();

        PisoResponse piso = new PisoResponse(1L, "1", "Primer piso");

        SalaResponse response = SalaResponse.builder()
                .id(id)
                .nombre("Sala A101")
                .capacidad(30)
                .piso(piso)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(pisoClient.buscarPorId(1L)).thenReturn(piso);
        when(repository.save(existente)).thenReturn(actualizado);
        when(mapper.toResponse(actualizado, piso)).thenReturn(response);

        SalaResponse resultado = service.actualizar(id, request);

        assertEquals("Sala A101", resultado.getNombre());
    }

    @Test
    void testActualizar_PisoNoEncontrado() {
        Long id = 1L;
        Sala existente = Sala.builder()
                .id(id)
                .nombre("Sala Vieja")
                .capacidad(10)
                .idPiso(1L)
                .build();

        SalaRequest request = SalaRequest.builder()
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(99L)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(pisoClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.actualizar(id, request));
    }

    @Test
    void testActualizar_NotFound() {
        Long id = 99L;
        SalaRequest request = SalaRequest.builder()
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> service.actualizar(id, request));
    }

    @Test
    void testActualizarEntidad() {
        Long id = 1L;
        Sala existente = Sala.builder()
                .id(id)
                .nombre("Sala Vieja")
                .capacidad(10)
                .idPiso(1L)
                .build();
        SalaRequest request = SalaRequest.builder()
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();
        Sala actualizado = Sala.builder()
                .id(id)
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
                .build();

        PisoResponse piso = new PisoResponse(1L, "1", "Primer piso");

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(pisoClient.buscarPorId(1L)).thenReturn(piso);
        when(repository.save(existente)).thenReturn(actualizado);

        Sala resultado = service.actualizarEntidad(id, request);

        assertEquals("Sala A101", resultado.getNombre());
    }

    @Test
    void testActualizarEntidad_PisoNoEncontrado() {
        Long id = 1L;
        Sala existente = Sala.builder()
                .id(id)
                .nombre("Sala Vieja")
                .capacidad(10)
                .idPiso(1L)
                .build();

        SalaRequest request = SalaRequest.builder()
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(99L)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(pisoClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.actualizarEntidad(id, request));
    }

    @Test
    void testActualizarEntidad_NotFound() {
        Long id = 99L;
        SalaRequest request = SalaRequest.builder()
                .nombre("Sala A101")
                .capacidad(30)
                .idPiso(1L)
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
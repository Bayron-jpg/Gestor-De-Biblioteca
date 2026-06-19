package cl.duoc.turno_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import cl.duoc.turno_service.client.BibliotecarioClient;
import cl.duoc.turno_service.dto.BibliotecarioResponse;
import cl.duoc.turno_service.dto.TurnoRequest;
import cl.duoc.turno_service.dto.TurnoResponse;
import cl.duoc.turno_service.mapper.TurnoMapper;
import cl.duoc.turno_service.model.Turno;
import cl.duoc.turno_service.repository.TurnoRepository;

@SpringBootTest
@ActiveProfiles("test")
public class TurnoServiceTest {

    @Autowired
    private TurnoService service;

    @MockitoBean
    private TurnoRepository repository;

    @MockitoBean
    private TurnoMapper mapper;

    @MockitoBean
    private BibliotecarioClient bibliotecarioClient;

    @Test
    public void testGetAll() {
        Turno turno = Turno.builder()
                .id(1L)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        BibliotecarioResponse bibliotecario = new BibliotecarioResponse(1L, "Carla", "Soto", 35);

        TurnoResponse response = TurnoResponse.builder()
                .id(1L)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .bibliotecario(bibliotecario)
                .build();

        when(repository.findAll()).thenReturn(List.of(turno));
        when(bibliotecarioClient.buscarPorId(1L)).thenReturn(bibliotecario);
        when(mapper.toResponse(turno, bibliotecario)).thenReturn(response);

        List<TurnoResponse> turnos = service.getAll();
        assertNotNull(turnos);
        assertEquals(1, turnos.size());
        assertEquals("Mañana", turnos.get(0).getTurno());
    }

    @Test
    void testGetAllEntidad() {
        Turno turno = Turno.builder()
                .id(1L)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        when(repository.findAll()).thenReturn(List.of(turno));

        List<Turno> resultado = service.getAllEntidad();

        assertEquals(1, resultado.size());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Turno turno = Turno.builder()
                .id(1L)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        BibliotecarioResponse bibliotecario = new BibliotecarioResponse(1L, "Carla", "Soto", 35);

        TurnoResponse response = TurnoResponse.builder()
                .id(1L)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .bibliotecario(bibliotecario)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(turno));
        when(bibliotecarioClient.buscarPorId(1L)).thenReturn(bibliotecario);
        when(mapper.toResponse(turno, bibliotecario)).thenReturn(response);

        TurnoResponse encontrado = service.findById(id);
        assertNotNull(encontrado);
        assertEquals(id, encontrado.getId());
        assertEquals("Mañana", encontrado.getTurno());
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
        Turno turno = Turno.builder()
                .id(id)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(turno));

        Turno resultado = service.findByIdEntidad(id);

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
        TurnoRequest request = TurnoRequest.builder()
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        Turno turno = Turno.builder()
                .id(1L)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        BibliotecarioResponse bibliotecario = new BibliotecarioResponse(1L, "Carla", "Soto", 35);

        TurnoResponse response = TurnoResponse.builder()
                .id(1L)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .bibliotecario(bibliotecario)
                .build();

        when(bibliotecarioClient.buscarPorId(1L)).thenReturn(bibliotecario);
        when(mapper.toEntity(request)).thenReturn(turno);
        when(repository.save(turno)).thenReturn(turno);
        when(mapper.toResponse(turno, bibliotecario)).thenReturn(response);

        TurnoResponse guardado = service.crear(request);

        assertNotNull(guardado);
        assertEquals("Mañana", guardado.getTurno());
    }

    @Test
    void testCrear_BibliotecarioNoEncontrado() {
        TurnoRequest request = TurnoRequest.builder()
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(99L)
                .build();

        when(bibliotecarioClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.crear(request));
    }

    @Test
    void testCrearEntidad() {
        TurnoRequest request = TurnoRequest.builder()
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        Turno turno = Turno.builder()
                .id(1L)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        BibliotecarioResponse bibliotecario = new BibliotecarioResponse(1L, "Carla", "Soto", 35);

        when(bibliotecarioClient.buscarPorId(1L)).thenReturn(bibliotecario);
        when(mapper.toEntity(request)).thenReturn(turno);
        when(repository.save(turno)).thenReturn(turno);

        Turno resultado = service.crearEntidad(request);

        assertEquals("Mañana", resultado.getTurno());
    }

    @Test
    void testCrearEntidad_BibliotecarioNoEncontrado() {
        TurnoRequest request = TurnoRequest.builder()
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(99L)
                .build();

        when(bibliotecarioClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.crearEntidad(request));
    }

    @Test
    void testActualizar() {
        Long id = 1L;
        Turno existente = Turno.builder()
                .id(id)
                .turno("Tarde")
                .horaEntrada(LocalTime.of(14, 0))
                .horaSalida(LocalTime.of(20, 0))
                .idBibliotecario(1L)
                .build();

        TurnoRequest request = TurnoRequest.builder()
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        Turno actualizado = Turno.builder()
                .id(id)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        BibliotecarioResponse bibliotecario = new BibliotecarioResponse(1L, "Carla", "Soto", 35);

        TurnoResponse response = TurnoResponse.builder()
                .id(id)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .bibliotecario(bibliotecario)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(bibliotecarioClient.buscarPorId(1L)).thenReturn(bibliotecario);
        when(repository.save(existente)).thenReturn(actualizado);
        when(mapper.toResponse(actualizado, bibliotecario)).thenReturn(response);

        TurnoResponse resultado = service.actualizar(id, request);

        assertEquals("Mañana", resultado.getTurno());
    }

    @Test
    void testActualizar_NotFound() {
        Long id = 99L;
        TurnoRequest request = TurnoRequest.builder()
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> service.actualizar(id, request));
    }

    @Test
    void testActualizar_BibliotecarioNoEncontrado() {
        Long id = 1L;
        Turno existente = Turno.builder()
                .id(id)
                .turno("Tarde")
                .horaEntrada(LocalTime.of(14, 0))
                .horaSalida(LocalTime.of(20, 0))
                .idBibliotecario(1L)
                .build();

        TurnoRequest request = TurnoRequest.builder()
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(99L)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(bibliotecarioClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.actualizar(id, request));
    }

    @Test
    void testActualizarEntidad() {
        Long id = 1L;
        Turno existente = Turno.builder()
                .id(id)
                .turno("Tarde")
                .horaEntrada(LocalTime.of(14, 0))
                .horaSalida(LocalTime.of(20, 0))
                .idBibliotecario(1L)
                .build();

        TurnoRequest request = TurnoRequest.builder()
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        Turno actualizado = Turno.builder()
                .id(id)
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        BibliotecarioResponse bibliotecario = new BibliotecarioResponse(1L, "Carla", "Soto", 35);

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(bibliotecarioClient.buscarPorId(1L)).thenReturn(bibliotecario);
        when(repository.save(existente)).thenReturn(actualizado);

        Turno resultado = service.actualizarEntidad(id, request);

        assertEquals("Mañana", resultado.getTurno());
    }

    @Test
    void testActualizarEntidad_NotFound() {
        Long id = 99L;
        TurnoRequest request = TurnoRequest.builder()
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(1L)
                .build();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> service.actualizarEntidad(id, request));
    }

    @Test
    void testActualizarEntidad_BibliotecarioNoEncontrado() {
        Long id = 1L;
        Turno existente = Turno.builder()
                .id(id)
                .turno("Tarde")
                .horaEntrada(LocalTime.of(14, 0))
                .horaSalida(LocalTime.of(20, 0))
                .idBibliotecario(1L)
                .build();

        TurnoRequest request = TurnoRequest.builder()
                .turno("Mañana")
                .horaEntrada(LocalTime.of(8, 0))
                .horaSalida(LocalTime.of(14, 0))
                .idBibliotecario(99L)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(bibliotecarioClient.buscarPorId(99L)).thenReturn(null);

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
package cl.duoc.libro_service.service;

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

import cl.duoc.libro_service.client.AutorClient;
import cl.duoc.libro_service.client.GeneroClient;
import cl.duoc.libro_service.dto.AutorResponse;
import cl.duoc.libro_service.dto.GeneroResponse;
import cl.duoc.libro_service.dto.LibroRequest;
import cl.duoc.libro_service.dto.LibroResponse;
import cl.duoc.libro_service.mapper.LibroMapper;
import cl.duoc.libro_service.model.Libro;
import cl.duoc.libro_service.repository.LibroRepository;

@SpringBootTest
@ActiveProfiles("test")
public class LibroServiceTest {

    @Autowired
    private LibroService service;

    @MockitoBean
    private LibroRepository repository;

    @MockitoBean
    private LibroMapper mapper;

    @MockitoBean
    private AutorClient autorClient;

    @MockitoBean
    private GeneroClient generoClient;

    @Test
    public void testGetAll() {
        Libro libro = Libro.builder()
                .id(1L)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        AutorResponse autor = new AutorResponse(1L, "Gabriel García Márquez", LocalDate.of(1927, 3, 6));
        GeneroResponse genero = new GeneroResponse(1L, "Realismo mágico",
                "Mezcla de elementos fantásticos con la realidad.");

        LibroResponse response = LibroResponse.builder()
                .id(1L)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .autor(autor)
                .genero(genero)
                .build();

        when(repository.findAll()).thenReturn(List.of(libro));
        when(autorClient.buscarPorId(1L)).thenReturn(autor);
        when(generoClient.buscarPorId(1L)).thenReturn(genero);
        when(mapper.toResponse(libro, autor, genero)).thenReturn(response);

        List<LibroResponse> libros = service.getAll();
        assertNotNull(libros);
        assertEquals(1, libros.size());
        assertEquals("Cien años de soledad", libros.get(0).getTitulo());
    }

    @Test
    void testGetAllEntidad() {
        Libro libro = Libro.builder()
                .id(1L)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        when(repository.findAll()).thenReturn(List.of(libro));

        List<Libro> resultado = service.getAllEntidad();

        assertEquals(1, resultado.size());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Libro libro = Libro.builder()
                .id(1L)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        AutorResponse autor = new AutorResponse(1L, "Gabriel García Márquez", LocalDate.of(1927, 3, 6));
        GeneroResponse genero = new GeneroResponse(1L, "Realismo mágico",
                "Mezcla de elementos fantásticos con la realidad.");

        LibroResponse response = LibroResponse.builder()
                .id(1L)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .autor(autor)
                .genero(genero)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(libro));
        when(autorClient.buscarPorId(1L)).thenReturn(autor);
        when(generoClient.buscarPorId(1L)).thenReturn(genero);
        when(mapper.toResponse(libro, autor, genero)).thenReturn(response);

        LibroResponse encontrado = service.findById(id);
        assertNotNull(encontrado);
        assertEquals(id, encontrado.getId());
        assertEquals("Cien años de soledad", encontrado.getTitulo());
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
        Libro libro = Libro.builder()
                .id(id)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(libro));

        Libro resultado = service.findByIdEntidad(id);

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
        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();
        Libro libro = Libro.builder()
                .id(1L)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        AutorResponse autor = new AutorResponse(1L, "Gabriel García Márquez", LocalDate.of(1927, 3, 6));
        GeneroResponse genero = new GeneroResponse(1L, "Realismo mágico",
                "Mezcla de elementos fantásticos con la realidad.");

        LibroResponse response = LibroResponse.builder()
                .id(1L)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .autor(autor)
                .genero(genero)
                .build();

        when(autorClient.buscarPorId(1L)).thenReturn(autor);
        when(generoClient.buscarPorId(1L)).thenReturn(genero);
        when(mapper.toEntity(request)).thenReturn(libro);
        when(repository.save(libro)).thenReturn(libro);
        when(mapper.toResponse(libro, autor, genero)).thenReturn(response);

        LibroResponse guardado = service.crear(request);

        assertNotNull(guardado);
        assertEquals("Cien años de soledad", guardado.getTitulo());
    }

    @Test
    void testCrear_AutorNoEncontrado() {
        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(99L)
                .idGenero(1L)
                .build();

        when(autorClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.crear(request));
    }

    @Test
    void testCrear_GeneroNoEncontrado() {
        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(99L)
                .build();
        AutorResponse autor = new AutorResponse(1L, "Gabriel García Márquez", LocalDate.of(1927, 3, 6));

        when(autorClient.buscarPorId(1L)).thenReturn(autor);
        when(generoClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.crear(request));
    }

    @Test
    void testCrearEntidad() {
        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();
        Libro libro = Libro.builder()
                .id(1L)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        AutorResponse autor = new AutorResponse(1L, "Gabriel García Márquez", LocalDate.of(1927, 3, 6));
        GeneroResponse genero = new GeneroResponse(1L, "Realismo mágico",
                "Mezcla de elementos fantásticos con la realidad.");

        when(autorClient.buscarPorId(1L)).thenReturn(autor);
        when(generoClient.buscarPorId(1L)).thenReturn(genero);
        when(mapper.toEntity(request)).thenReturn(libro);
        when(repository.save(libro)).thenReturn(libro);

        Libro resultado = service.crearEntidad(request);

        assertEquals("Cien años de soledad", resultado.getTitulo());
    }

    @Test
    void testActualizar_AutorNoEncontrado() {
        Long id = 1L;
        Libro existente = Libro.builder()
                .id(id)
                .titulo("Viejo Título")
                .isbn("0000000000000")
                .fechaPublicacion(LocalDate.of(1950, 1, 1))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(99L)
                .idGenero(1L)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(autorClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.actualizar(id, request));
    }

    @Test
    void testActualizar_GeneroNoEncontrado() {
        Long id = 1L;
        Libro existente = Libro.builder()
                .id(id)
                .titulo("Viejo Título")
                .isbn("0000000000000")
                .fechaPublicacion(LocalDate.of(1950, 1, 1))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(99L)
                .build();

        AutorResponse autor = new AutorResponse(1L, "Gabriel García Márquez", LocalDate.of(1927, 3, 6));

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(autorClient.buscarPorId(1L)).thenReturn(autor);
        when(generoClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.actualizar(id, request));
    }

    @Test
    void testActualizarEntidad_AutorNoEncontrado() {
        Long id = 1L;
        Libro existente = Libro.builder()
                .id(id)
                .titulo("Viejo Título")
                .isbn("0000000000000")
                .fechaPublicacion(LocalDate.of(1950, 1, 1))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(99L)
                .idGenero(1L)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(autorClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.actualizarEntidad(id, request));
    }

    @Test
    void testActualizarEntidad_GeneroNoEncontrado() {
        Long id = 1L;
        Libro existente = Libro.builder()
                .id(id)
                .titulo("Viejo Título")
                .isbn("0000000000000")
                .fechaPublicacion(LocalDate.of(1950, 1, 1))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(99L)
                .build();

        AutorResponse autor = new AutorResponse(1L, "Gabriel García Márquez", LocalDate.of(1927, 3, 6));

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(autorClient.buscarPorId(1L)).thenReturn(autor);
        when(generoClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.actualizarEntidad(id, request));
    }

    @Test
    void testCrearEntidad_AutorNoEncontrado() {
        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(99L)
                .idGenero(1L)
                .build();

        when(autorClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.crearEntidad(request));
    }

    @Test
    void testCrearEntidad_GeneroNoEncontrado() {
        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(99L)
                .build();

        AutorResponse autor = new AutorResponse(1L, "Gabriel García Márquez", LocalDate.of(1927, 3, 6));

        when(autorClient.buscarPorId(1L)).thenReturn(autor);
        when(generoClient.buscarPorId(99L)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> service.crearEntidad(request));
    }

    @Test
    void testActualizar() {
        Long id = 1L;
        Libro existente = Libro.builder()
                .id(id)
                .titulo("Viejo Título")
                .isbn("0000000000000")
                .fechaPublicacion(LocalDate.of(1950, 1, 1))
                .idAutor(1L)
                .idGenero(1L)
                .build();
        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();
        Libro actualizado = Libro.builder()
                .id(id)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        AutorResponse autor = new AutorResponse(1L, "Gabriel García Márquez", LocalDate.of(1927, 3, 6));
        GeneroResponse genero = new GeneroResponse(1L, "Realismo mágico",
                "Mezcla de elementos fantásticos con la realidad.");

        LibroResponse response = LibroResponse.builder()
                .id(id)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .autor(autor)
                .genero(genero)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(autorClient.buscarPorId(1L)).thenReturn(autor);
        when(generoClient.buscarPorId(1L)).thenReturn(genero);
        when(repository.save(existente)).thenReturn(actualizado);
        when(mapper.toResponse(actualizado, autor, genero)).thenReturn(response);

        LibroResponse resultado = service.actualizar(id, request);

        assertEquals("Cien años de soledad", resultado.getTitulo());
    }

    @Test
    void testActualizar_NotFound() {
        Long id = 99L;
        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> service.actualizar(id, request));
    }

    @Test
    void testActualizarEntidad() {
        Long id = 1L;
        Libro existente = Libro.builder()
                .id(id)
                .titulo("Viejo Título")
                .isbn("0000000000000")
                .fechaPublicacion(LocalDate.of(1950, 1, 1))
                .idAutor(1L)
                .idGenero(1L)
                .build();
        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();
        Libro actualizado = Libro.builder()
                .id(id)
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
                .build();
        AutorResponse autor = new AutorResponse(1L, "Gabriel García Márquez", LocalDate.of(1927, 3, 6));
        GeneroResponse genero = new GeneroResponse(1L, "Realismo mágico",
                "Mezcla de elementos fantásticos con la realidad.");

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(autorClient.buscarPorId(1L)).thenReturn(autor);
        when(generoClient.buscarPorId(1L)).thenReturn(genero);
        when(repository.save(existente)).thenReturn(actualizado);

        Libro resultado = service.actualizarEntidad(id, request);

        assertEquals("Cien años de soledad", resultado.getTitulo());
    }

    @Test
    void testActualizarEntidad_NotFound() {
        Long id = 99L;
        LibroRequest request = LibroRequest.builder()
                .titulo("Cien años de soledad")
                .isbn("9780307474728")
                .fechaPublicacion(LocalDate.of(1967, 5, 30))
                .idAutor(1L)
                .idGenero(1L)
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
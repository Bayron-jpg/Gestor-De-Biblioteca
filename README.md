# Sistema de Gestión para Biblioteca

Sistema de gestión para bibliotecas basado en arquitectura de microservicios.
Permite administrar autores, géneros, libros, usuarios, bibliotecarios, pisos y salas de forma distribuida,
donde cada dominio opera de manera independiente y se comunica a través de una API Gateway
centralizada con registro de servicios mediante Eureka.

Desarrollado con Java 21 y Spring Boot.

## Microservicios

### Grupo 1 — Gestión de Libros
| N°  | Microservicio    | Descripción                                             | Puerto |
| --- | ---------------- | ------------------------------------------------------- | ------ |
| 1.  | autor-service    | Gestiona los autores registrados en el sistema.         | 8081   |
| 2.  | genero-service   | Gestiona los géneros literarios disponibles.            | 8082   |
| 3.  | libro-service    | Gestiona los libros, depende de autor y género.         | 8083   |

### Grupo 2 — Gestión de Personal
| N°  | Microservicio         | Descripción                                    | Puerto |
| --- | --------------------- | ---------------------------------------------- | ------ |
| 4.  | bibliotecario-service | Gestiona el personal de la biblioteca.         | 8084   |
| 5.  | turno-service         | Gestiona los turnos del bibliotecario.         | 8085   |

### Grupo 3 — Gestión de Usuarios
| N°  | Microservicio     | Descripción                                             | Puerto |
| --- | ----------------- | ------------------------------------------------------- | ------ |
| 6.  | telefono-service  | Gestiona los teléfonos de contacto de los usuarios.     | 8086   |
| 7.  | direccion-service | Gestiona las direcciones de los usuarios.               | 8087   |
| 8.  | usuario-service   | Gestiona los usuarios, depende de teléfono y dirección. | 8088   |

### Grupo 4 — Gestión de Instalaciones
| N°  | Microservicio  | Descripción                                          | Puerto |
| --- | -------------- | ---------------------------------------------------- | ------ |
| 9.  | piso-service   | Gestiona los pisos de la biblioteca.                 | 8089   |
| 10. | sala-service   | Gestiona las salas disponibles, depende del piso.    | 8090   |

### Infraestructura
| N°  | Servicio        | Descripción                                                            | Puerto |
| --- | --------------- | ---------------------------------------------------------------------- | ------ |
| 11. | API Gateway     | Punto de entrada único que enruta las peticiones a los microservicios. | 8080   |
| 12. | Eureka Server   | Servidor de registro y descubrimiento de microservicios.               | 8761   |

## Rutas del API Gateway
> Todas las peticiones se realizan a través del puerto 8080.

### Grupo 1 — Gestión de Libros

| Método | Ruta                        | Descripción                        | Microservicio  |
| ------ | --------------------------- | ---------------------------------- | -------------- |
| GET    | /api/v1/autores             | Obtiene todos los autores          | autor-service  |
| GET    | /api/v1/autores/{id}        | Obtiene un autor por ID            | autor-service  |
| POST   | /api/v1/autores             | Crea un nuevo autor                | autor-service  |
| PUT    | /api/v1/autores/{id}        | Actualiza un autor por ID          | autor-service  |
| DELETE | /api/v1/autores/{id}        | Elimina un autor por ID            | autor-service  |
| GET    | /api/v1/generos             | Obtiene todos los géneros          | genero-service |
| GET    | /api/v1/generos/{id}        | Obtiene un género por ID           | genero-service |
| POST   | /api/v1/generos             | Crea un nuevo género               | genero-service |
| PUT    | /api/v1/generos/{id}        | Actualiza un género por ID         | genero-service |
| DELETE | /api/v1/generos/{id}        | Elimina un género por ID           | genero-service |
| GET    | /api/v1/libros              | Obtiene todos los libros           | libro-service  |
| GET    | /api/v1/libros/{id}         | Obtiene un libro por ID            | libro-service  |
| POST   | /api/v1/libros              | Crea un nuevo libro                | libro-service  |
| PUT    | /api/v1/libros/{id}         | Actualiza un libro por ID          | libro-service  |
| DELETE | /api/v1/libros/{id}         | Elimina un libro por ID            | libro-service  |

### Grupo 2 — Gestión de Personal

| Método | Ruta                             | Descripción                            | Microservicio         |
| ------ | -------------------------------- | -------------------------------------- | --------------------- |
| GET    | /api/v1/bibliotecarios           | Obtiene todos los bibliotecarios       | bibliotecario-service |
| GET    | /api/v1/bibliotecarios/{id}      | Obtiene un bibliotecario por ID        | bibliotecario-service |
| POST   | /api/v1/bibliotecarios           | Crea un nuevo bibliotecario            | bibliotecario-service |
| PUT    | /api/v1/bibliotecarios/{id}      | Actualiza un bibliotecario por ID      | bibliotecario-service |
| DELETE | /api/v1/bibliotecarios/{id}      | Elimina un bibliotecario por ID        | bibliotecario-service |
| GET    | /api/v1/turnos                   | Obtiene todos los turnos               | turno-service         |
| GET    | /api/v1/turnos/{id}              | Obtiene un turno por ID                | turno-service         |
| POST   | /api/v1/turnos                   | Crea un nuevo turno                    | turno-service         |
| PUT    | /api/v1/turnos/{id}              | Actualiza un turno por ID              | turno-service         |
| DELETE | /api/v1/turnos/{id}              | Elimina un turno por ID                | turno-service         |

### Grupo 3 — Gestión de Usuarios

| Método | Ruta                        | Descripción                            | Microservicio     |
| ------ | --------------------------- | -------------------------------------- | ----------------- |
| GET    | /api/v1/telefonos           | Obtiene todos los teléfonos            | telefono-service  |
| GET    | /api/v1/telefonos/{id}      | Obtiene un teléfono por ID             | telefono-service  |
| POST   | /api/v1/telefonos           | Crea un nuevo teléfono                 | telefono-service  |
| PUT    | /api/v1/telefonos/{id}      | Actualiza un teléfono por ID           | telefono-service  |
| DELETE | /api/v1/telefonos/{id}      | Elimina un teléfono por ID             | telefono-service  |
| GET    | /api/v1/direcciones         | Obtiene todas las direcciones          | direccion-service |
| GET    | /api/v1/direcciones/{id}    | Obtiene una dirección por ID           | direccion-service |
| POST   | /api/v1/direcciones         | Crea una nueva dirección               | direccion-service |
| PUT    | /api/v1/direcciones/{id}    | Actualiza una dirección por ID         | direccion-service |
| DELETE | /api/v1/direcciones/{id}    | Elimina una dirección por ID           | direccion-service |
| GET    | /api/v1/usuarios            | Obtiene todos los usuarios             | usuario-service   |
| GET    | /api/v1/usuarios/{id}       | Obtiene un usuario por ID              | usuario-service   |
| POST   | /api/v1/usuarios            | Crea un nuevo usuario                  | usuario-service   |
| PUT    | /api/v1/usuarios/{id}       | Actualiza un usuario por ID            | usuario-service   |
| DELETE | /api/v1/usuarios/{id}       | Elimina un usuario por ID              | usuario-service   |

### Grupo 4 — Gestión de Instalaciones

| Método | Ruta                    | Descripción                        | Microservicio  |
| ------ | ----------------------- | ---------------------------------- | -------------- |
| GET    | /api/v1/pisos           | Obtiene todos los pisos            | piso-service   |
| GET    | /api/v1/pisos/{id}      | Obtiene un piso por ID             | piso-service   |
| POST   | /api/v1/pisos           | Crea un nuevo piso                 | piso-service   |
| PUT    | /api/v1/pisos/{id}      | Actualiza un piso por ID           | piso-service   |
| DELETE | /api/v1/pisos/{id}      | Elimina un piso por ID             | piso-service   |
| GET    | /api/v1/salas           | Obtiene todas las salas            | sala-service   |
| GET    | /api/v1/salas/{id}      | Obtiene una sala por ID            | sala-service   |
| POST   | /api/v1/salas           | Crea una nueva sala                | sala-service   |
| PUT    | /api/v1/salas/{id}      | Actualiza una sala por ID          | sala-service   |
| DELETE | /api/v1/salas/{id}      | Elimina una sala por ID            | sala-service   |

## Documentación Swagger

> Cada microservicio expone su propia documentación Swagger en su puerto correspondiente.

| N°  | Microservicio         | Swagger UI                        |
| --- | --------------------- | --------------------------------- |
| 1.  | autor-service         | http://localhost:8081/doc         |
| 2.  | genero-service        | http://localhost:8082/doc         |
| 3.  | libro-service         | http://localhost:8083/doc         |
| 4.  | bibliotecario-service | http://localhost:8084/doc         |
| 5.  | turno-service         | http://localhost:8085/doc         |
| 6.  | telefono-service      | http://localhost:8086/doc         |
| 7.  | direccion-service     | http://localhost:8087/doc         |
| 8.  | usuario-service       | http://localhost:8088/doc         |
| 9.  | piso-service          | http://localhost:8089/doc         |
| 10. | sala-service          | http://localhost:8090/doc         |

## Instrucciones de Ejecución Local/Remota
### Ejecución Local (Docker)
### Requisitos
- Docker Desktop instalado
- Docker Compose (incluido en Docker Desktop)

### Pasos
1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Bayron-jpg/Gestor-De-Biblioteca
   cd Gestor-De-Biblioteca
   ```
2. **Levantar todo el sistema**
   - `docker compose up --build`
   - `docker compose down -v (para limpiar)`

3. **Verificar el sistema**
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080

4. **Probar los endpoints**
   - Importar la colección de Postman (`Postman_collection`) y probar:
     - **A través del API Gateway:** `http://localhost:8080/api/v1/...`
     - **Directo a cada microservicio:** usando el puerto correspondiente (ej. `http://localhost:8083/api/v1/libros`)

### Ejecución Remota
> El sistema está configurado para ejecución local mediante Docker Compose.
> No se incluye despliegue remoto en esta entrega.

## Tecnologías
| Dependencia         | Uso                                                      |
| ------------------- | -------------------------------------------------------- |
| Lombok              | Reducción de código repetitivo.                          |
| Spring Web          | Endpoints REST y controladores.                          |
| Spring Data JPA     | Persistencia y operaciones CRUD.                         |
| Validation          | Validación de datos (Bean Validation).                   |
| MySQL Driver        | Conexión con base de datos MySQL.                        |
| Flyway Migration    | Migraciones y versionado de base de datos.               |
| Spring Reactive Web | Comunicación entre microservicios.                       |
| SpringDoc OpenAPI   | Documentación automática de la API REST (Swagger UI).    |
| Spring HATEOAS      | Links hipermedia en respuestas REST.                     |
| DataFaker           | Generación de datos falsos para desarrollo.              |
| Mockito             | Simulación de dependencias en pruebas unitarias.         |
| JUnit Jupiter       | Framework de pruebas unitarias.                          |
| H2 Database         | Base de datos en memoria para pruebas unitarias.         |
| Eureka Server          | Servidor de registro y descubrimiento de servicios.   |
| Eureka Discovery Client| Registra el microservicio en el servidor Eureka.      |
| JaCoCo               | Mide la cobertura de pruebas unitarias del código.    |

## Cobertura de Pruebas
Las pruebas unitarias se encuentran en cada microservicio bajo `target/site/jacoco/index.html`

## Integrantes
|   | Nombres          | Apellidos      |
| - | ---------------- | -------------- |
| 1 | Bayron Alexander | Urrutia Flores |

# Galileo

Proyecto Spring Boot de ejemplo que provee una API REST para gestionar usuarios y productos.

## Características

- API REST basada en Spring Web MVC
- Persistencia con Spring Data JPA
- Base de datos en memoria H2
- Validación de peticiones con Jakarta Validation
- Documentación OpenAPI / Swagger UI
- Autenticación y control de acceso con JWT y Spring Security
- Listados paginados con `page`, `size` y `sort`
- Respuestas desacopladas de las entidades JPA mediante DTOs

## Endpoints principales

Auth:
- `POST /api/auth/login` - Autenticación con correo y contraseña, devuelve token JWT
- `POST /api/auth/logout` - Invalida el token JWT actual

Usuarios:
- `GET /api/users` (paginado) - Requiere token Bearer con rol `ADMIN`
- `GET /api/users/{id}` - Requiere token Bearer con rol `ADMIN`
- `POST /api/users` - Requiere token Bearer con rol `ADMIN`
- `PUT /api/users/{id}` - Requiere token Bearer con rol `ADMIN`
- `DELETE /api/users/{id}` - Requiere token Bearer con rol `ADMIN`

Productos:
- `GET /api/products` (paginado) - Requiere token Bearer válido
- `GET /api/products/{id}` - Requiere token Bearer válido
- `POST /api/products` - Requiere token Bearer válido
- `PUT /api/products/{id}` - Requiere token Bearer válido
- `DELETE /api/products/{id}` - Requiere token Bearer válido

## Requisitos

- Java 25
- Maven (o el wrapper incluido `./mvnw`)

## Ejecución

Desde la raíz del proyecto:

```bash
./mvnw spring-boot:run
```

Luego abrir:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`

## Configuración

La configuración principal se encuentra en `src/main/resources/application.properties`.

- Base de datos H2 en memoria: `jdbc:h2:mem:copilotdb`
- JPA DDL automático: `spring.jpa.hibernate.ddl-auto=update`
- Ruta de Swagger: `/swagger-ui.html`
- Control de acceso JWT token: `security.jwt.secret` y `security.jwt.expiration-ms`

> Para consumir los endpoints protegidos, envía el token en el header `Authorization: Bearer {token}`.

## Pruebas

Ejecutar pruebas unitarias y de integración con:

```bash
./mvnw test
```

## Linter / Checkstyle

Este proyecto usa Checkstyle con la configuración en `config/checkstyle/checkstyle.xml`.
Para validar el estilo, ejecuta:

```bash
./mvnw clean verify
```

O solamente el chequeo de Checkstyle:

```bash
./mvnw checkstyle:check
```

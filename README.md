# Galileo

Proyecto Spring Boot de ejemplo que provee una API REST para gestionar usuarios y productos.

## Características

- API REST basada en Spring Web MVC
- Persistencia con Spring Data JPA
- Base de datos en memoria H2
- Validación de peticiones con Jakarta Validation
- Documentación OpenAPI / Swagger UI
- Listados paginados con `page`, `size` y `sort`
- Respuestas desacopladas de las entidades JPA mediante DTOs

## Endpoints principales

Usuarios:
- `GET /api/users` (paginado)
- `GET /api/users/{id}`
- `POST /api/users`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

Productos:
- `GET /api/products` (paginado)
- `GET /api/products/{id}`
- `POST /api/products`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`

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

## Pruebas

Ejecutar pruebas unitarias y de integración con:

```bash
./mvnw test
```

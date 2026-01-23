# Panama Trips API

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-brightgreen.svg)](https://www.postgresql.org/)

## Descripción
Sistema de gestión de reservas de viajes y paseos turísticos por Panamá. API RESTful para manejar tours, reservas, proveedores, pagos, usuarios, reseñas y geografía panameña (provincias, comarcas, etc.).

## Tecnologías
- **Backend**: Spring Boot 3.4.2 (Web, JPA, Security, Validation)
- **DB**: PostgreSQL + Flyway migraciones + Hibernate
- **Auth**: JWT (Auth0)
- **Docs**: Swagger/OpenAPI
- **Otros**: Lombok, Maven

## Estructura del Proyecto
```
src/
├── main/
│   ├── java/com/app/panama_trips/
│   │   ├── persistence/ (Entidades JPA + Repos)
│   │   ├── presentation/ (Controladores REST + DTOs)
│   │   ├── service/ (Interfaces + Impl)
│   │   ├── security/ (JWT, Config)
│   │   └── exception/ (Manejo errores)
│   └── resources/ (application.properties + DB migrations)
└── test/ (Pruebas unitarias/integrales completas)
```

## Requisitos
- Java 21
- Maven 3.9+
- PostgreSQL 15+ (DB: `panama_trips`, user/pass: `root/root`)
- IDE: IntelliJ/VSCode (archivos .idea/.vscode presentes)

## Instalación y Ejecución
1. Clona el repo:
   ```
   git clone <repo-url>
   cd panama-trips
   ```
2. Configura DB:
   - Crea DB `panama_trips`.
   - Flyway aplicará migraciones automáticamente.
3. Ejecuta:
   ```
   ./mvnw spring-boot:run
   ```
   - Puerto: 8080 (default).
4. Swagger UI: http://localhost:8080/swagger-ui.html

## Configuración
- `application.properties`: Ajusta DB/JWT.
- JWT Secret: Cambia en prod (actual: hardcoded).

## Endpoints Principales
- **Auth**: `/auth/login`, `/auth/register`
- **Tours**: `/tour-plans` (CRUD, disponibilidad, precios)
- **Reservas**: `/reservations`
- **Usuarios/Proveedores**: `/users`, `/providers`
- Ver Swagger para detalles.

## Pruebas
```
./mvnw test
```
Cobertura alta en servicios/controladores.

## Despliegue
- JAR: `./mvnw clean package`
- Docker: Agrega Dockerfile.
- Cloud: Heroku/Railway (Postgres addon).

## Contribuciones
1. Fork → Branch → PR.
2. Sigue convención: PascalCase clases, camelCase métodos.

## Licencia
MIT (agrega LICENSE).

¡Gracias por explorar Panama Trips! 🌴✈️

# AGENTS.md

## Tech Stack
- **Java:** Version 25 (NEVER use older API features).
- **Framework:** Spring Boot 4.
- **Database:** H2.

## Commands (Do not guess commands)
- **Run:** `./mvnw spring-boot:run`
- **Clean:** `./mvnw clean`
- **Test:** `./mvnw test`
- **Unit tests:** `./mvnw test`

## Architecture & Code Conventions
- STRICT 3-layer architecture: Controller -> Service -> Repository.
- `@RestController` classes MUST ONLY communicate with `@Service` classes.
- NEVER inject a `@Repository` directly into a Controller.
- Dependency Injection: ALWAYS use constructor injection or Lombok's `@RequiredArgsConstructor`. 
- NEVER use field injection (`@Autowired`).
- Security must be implemented with Spring Security and JWT for endpoint access control.
- Controllers should document bearer authentication in OpenAPI when endpoints require a JWT token.

## Workflow Rules
- **Small Changes:** Prioritize small, highly readable code changes.
- **OpenAPI:** When adding or modifying endpoints, validate them using `springdoc-openapi` annotations.
- **Testing:** If you add new routes or logic, you MUST write or update the corresponding unit tests and run `./mvnw test` before submitting.
- **Linting:** Respect the Checkstyle configuration in `config/checkstyle/checkstyle.xml`; run `./mvnw clean verify` or `./mvnw checkstyle:check` before submitting.
- **Context:** Check `README.md` for setup instructions and `src/main/java/com/example/galileo/controller` to understand existing endpoint designs before proposing new ones.
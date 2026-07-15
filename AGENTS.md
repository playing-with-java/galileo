# AGENTS.md

## Tech Stack
- **Java:** Version 25 (NEVER use older API features).
- **Framework:** Spring Boot 4.
- **Database:** H2.

## Commands (Do not guess commands)
- **Run:** `./mvnw spring-boot:run`
- **Clean:** `./mvnw clean`
- **Test:** `./mvnw test`

## Architecture & Code Conventions
- STRICT 3-layer architecture: Controller -> Service -> Repository.
- `@RestController` classes MUST ONLY communicate with `@Service` classes.
- NEVER inject a `@Repository` directly into a Controller.
- Dependency Injection: ALWAYS use constructor injection or Lombok's `@RequiredArgsConstructor`. 
- NEVER use field injection (`@Autowired`).

## Workflow Rules
- **Small Changes:** Prioritize small, highly readable code changes.
- **OpenAPI:** When adding or modifying endpoints, validate them using `springdoc-openapi` annotations.
- **Testing:** If you add new routes or logic, you MUST write or update the corresponding tests.
- **Context:** Check `README.md` for setup instructions and `src/main/java/com/example/galileo/controller` to understand existing endpoint designs before proposing new ones.
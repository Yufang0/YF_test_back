---
applyTo: "**/*.{java,kt}"
---

# ERP Back‑End • Spring Boot 3.5.4 Coding Rules (Copilot)

> Follow every rule exactly. Do not suggest alternatives unless asked. Assume Java 21 + Maven 3.9.11 + Spring Boot 3.5.4.

## 📦 Build & Dependency Rules
- **Parent POM**: `org.springframework.boot:spring-boot-starter-parent:3.5.4`.
- **Java**: set `<java.version>21</java.version>` **and** `<maven.compiler.release>21</maven.compiler.release>`.
- **Mandatory starters**: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, `spring-boot-starter-security`, `spring-boot-starter-test` (scope test).
- **OpenAPI**: add `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9`.
- Use Jakarta EE 10 dependencies only; never import `javax.*`.
- Enable container images with the Maven goal `spring-boot:build-image`.

## 🔧 Configuration Defaults
- Application config file: `application.yml`.
- Enable virtual threads: `spring.threads.virtual.enabled=true`.
- JPA: `spring.jpa.hibernate.ddl-auto=validate` by default; avoid `create`/`update` in production.
- Expose OpenAPI on actuator port: `springdoc.show-actuator=true` if management port differs.

## 🏗️ Project Structure
```
com.example.erp
 ├─ domain      // JPA entities (Jakarta annotations)
 ├─ repository  // Spring Data JPA interfaces
 ├─ service     // @Service business logic (constructor‑based DI)
 ├─ web         // @RestController + DTO objects
 └─ config      // SecurityConfig, OpenApiConfig, etc.
```

## ✍️ Coding Conventions
- Use **constructor injection** only; no `@Autowired` on fields.
- Use `record` classes for immutable DTOs where appropriate.
- Validate input with Jakarta Bean Validation (`@NotNull`, `@Size`…).
- Global error handling via `@ControllerAdvice` + `@ExceptionHandler` returning RFC 7807 Problem Details JSON.
- Security: Spring Security 6; prefer JWT Bearer auth for APIs, HTTP Basic only for internal endpoints.

## 🧪 Testing
- Use JUnit 5 (`@SpringBootTest(webEnvironment = RANDOM_PORT)`).
- Integrate Testcontainers for database integration tests.
- Use Mockito/AssertJ; avoid PowerMock.

## 🚀 Deployment & Observability
- Health/metrics via Spring Boot Actuator (`/actuator/health`, `/actuator/metrics`).
- Package as OCI image with Buildpacks (`paketobuildpacks/builder-jammy-base`).
- Add Prometheus & OpenTelemetry exporters only when needed; keep disabled by default.

## ⚠️ Upgrade / Compatibility Checklist
1. Remove all `javax.*` imports → replace with `jakarta.*`.
2. Ensure third‑party libs are Spring Boot 3.x compatible.
3. Update Maven plugins that fail on Java 21.

## ✅ Commit Gate
- Build must pass `mvn verify -B`.
- All tests green and coverage ≥ 80 %.
- Lint with `maven-checkstyle-plugin`.

---


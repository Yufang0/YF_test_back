---
applyTo: "**/*.{java,kt}"
---

# ERP Backend • Spring Boot 3.5.4 Coding Standards

Follow the [General Coding Guidelines](./general_coding_guide.instructions.md)

> Follow **every** rule exactly. Do **not** suggest alternative approaches unless explicitly asked.  
> Target stack: **Java 21 + Maven 3.9.11 + Spring Boot 3.5.4** (Jakarta EE 10).

---

## 📦 Build & Dependency Rules

- **Parent POM**: `org.springframework.boot:spring-boot-starter-parent:3.5.4`.
- **Java Version**: Set both `<java.version>21</java.version>` **and** `<maven.compiler.release>21</maven.compiler.release>`.
- **Required Starters**: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, `spring-boot-starter-security`, `spring-boot-starter-test` (test scope).
- **OpenAPI Documentation**: Add `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9`.
- **Jakarta EE Only**: Use Jakarta EE 10 dependencies exclusively; never import `javax.*`.
- **Container Images**: Enable with Maven goal `spring-boot:build-image`.

---

## 🔧 Configuration Defaults

- **Application Config**: Use `application.yml` as the primary configuration file. (not `.properties`)
- **Virtual Threads**: Enable with `spring.threads.virtual.enabled=true`.
- **JPA Settings**: Default to `spring.jpa.hibernate.ddl-auto=validate`; avoid `create`/`update` in production.
- **OpenAPI Actuator**: Set `springdoc.show-actuator=true` if management port differs from main port.

---

## 💡 Development Workflow Recommendations

### A. Model & Data Persistence

- Use JPA + Hibernate (or Spring Data JPA) to define Entity/Repository classes with Bean Validation (JSR-303) for data validation.
- Leverage Spring Boot's property binding to automatically load `application.properties` configurations.

### B. REST Controller Layer

- Implement `@RestController` + DTO pattern for data transfer.
- Use validation annotations like `@Validated`, `@NotNull`, `@Size` for input validation.

### C. Error Handling & Exceptions

- Implement global exception handling using `@ControllerAdvice` with unified error response format.

### D. Security & Authorization

- Use Spring Security with OAuth2 or Basic Authentication (as needed) to control API access.
- When using Spring Security OAuth2 client/server, ensure springdoc-openapi properly ignores `@AuthenticationPrincipal` endpoints.

### E. Testing

- Write unit and integration tests using Spring Boot's test starter: `spring-boot-starter-test`.
- For Native Image testing, refer to official native-image testing documentation.

---

## 🏗️ Project Structure

### Core Business Layers

```
com.systex.course/
├── entity/          # JPA Entity classes (XxxEntity)
├── dto/            # Data Transfer Objects (XxxDto)
├── vo/             # View Objects (XxxVo)
├── repo/           # Data Access Layer (XxxRepository)
├── service/        # Business Logic Layer (XxxService)
└── controller/     # Controller Layer (XxxController)
```

### Infrastructure Layers

```
com.systex.course/
├── config/         # Configuration classes (XxxConfig)
├── exception/      # Exception handling (XxxException)
├── constant/       # Constants & Enums (XxxEnum/XxxType)
├── util/          # Utility classes (XxxUtil/XxxHelper)
└── system/        # System-level components
    ├── filter/    # Filters (XxxFilter)
    ├── interceptor/ # Interceptors (XxxInterceptor)
    ├── aspect/    # Aspects (XxxAspect)
    └── handler/   # Handlers (XxxHandler)
```

### Naming Conventions

- **Entity**: Entity classes named as `XxxEntity`, corresponding to database tables
- **DTO**: Data Transfer Objects named as `XxxDto`, used for API requests/responses
- **VO**: View Objects named as `XxxVo`, used for frontend display
- **Repository**: Data access interfaces named as `XxxRepository`
- **Service**: Business logic classes named as `XxxService`
- **Controller**: Controller classes named as `XxxController`

### Unified Response Format

All API responses should follow the standard format:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... },         // 實際資料
  "timestamp": "2025-01-15T10:00:00Z",
  "path": "/api/v1/users"
}
```

### Pagination Response Format

```json
{
  "code": 200,
  "message": "查詢成功",
  "data": {
    "content": [...],      // 當前頁資料
    "page": 0,             // 當前頁碼 (0-based) (0-based)
    "size": 20,            // 每頁大小
    "totalElements": 100,  // 總記錄數
    "totalPages": 5,       // 總頁數
    "hasNext": true,       // 是否有下一頁
    "hasPrevious": false   // 是否有上一頁
  }
}
```

---

## ✍️ Coding Conventions

- **Dependency Injection**: Use constructor injection only; no `@Autowired` on fields.
- **Immutable DTOs**: Use `record` classes for immutable DTOs where appropriate.
- **Input Validation**: Validate input with Jakarta Bean Validation (`@NotNull`, `@Size`, etc.).
- **Global Error Handling**: Implement via `@ControllerAdvice` + `@ExceptionHandler` returning RFC 7807 Problem Details JSON.
- **Security**: Use Spring Security 6; prefer JWT Bearer auth for APIs, HTTP Basic only for internal endpoints.

---

## 🗄️ Database Design Standards

### Naming Conventions

- **Tables**: Lowercase with underscore separation (`snake_case`), e.g., `user_profile`
- **Primary Keys**: Uniformly named as `id`
- **Foreign Keys**: Format as `{referenced_table}_id`, e.g., `user_id`
- **Indexes**: Format as `idx_{table}_{column(s)}`
- **Constraints**: Format as `{constraint_type}_{table}_{column}`

### JPA Entity Best Practices

Entities **must** map column names explicitly and use `@CreationTimestamp` / `@UpdateTimestamp` for audit fields.

```java
@Entity
@Table(name = "user_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

---

## 📐 DTO/VO Design Standards

### DTO Standards (API Layer)

- DTOs expose API contract; annotate with Bean Validation and Jackson.

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    @JsonProperty("username")
    @NotBlank(message = "用戶名不能為空")
    @Size(min = 3, max = 50, message = "用戶名長度必須在3-50之間")
    private String username;

    @JsonProperty("email")
    @NotBlank(message = "郵箱不能為空")
    @Email(message = "Invalid email format")
    private String email;

    @JsonProperty("password")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
```

### VO Standards (View Layer)

- VOs are read models for UI; include display‑friendly formatting (e.g. `@JsonFormat`).

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileVo {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
```

---

## 🧪 Testing Standards

- Use JUnit 5 with `@SpringBootTest(webEnvironment = RANDOM_PORT)`.
- Integrate Testcontainers for database integration tests.
- Use Mockito/AssertJ; avoid PowerMock.
- Write both unit and integration tests using Spring Boot's test starter: `spring-boot-starter-test`.
- For Native Image testing, refer to official native-image testing documentation.

### Test Class Structure

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Find user by ID - Should return user when user exists")
    void testFindUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        UserEntity expectedUser = createTestUser(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        UserEntity actualUser = userService.findById(userId);

        // Assert
        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getId()).isEqualTo(userId);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Find user by ID - Should throw exception when user does not exist")
    void testFindUserById_WhenUserNotExists_ShouldThrowException() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.findById(userId))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage("User not found: " + userId);
    }
}
```

### Test Naming Conventions

- **Test Classes**: `{ClassUnderTest}Test`
- **Test Methods**: `test{MethodName}_{Condition}_{ExpectedResult}`
- **Display Names**: Use `@DisplayName` with descriptive English descriptions

### Test Coverage Goals

- **Primary Metric**: Line coverage >= 80%
- **Secondary Reference**: Branch coverage >= 70%, Method coverage >= 75%

---

## 🚀 Deployment & Observability

- **Health/Metrics**: Use Spring Boot Actuator (`/actuator/health`, `/actuator/metrics`).
- **Container Packaging**: Package as OCI image with Buildpacks (`paketobuildpacks/builder-jammy-base`).
- **Monitoring**: Add Prometheus & OpenTelemetry exporters only when needed; keep disabled by default.
- **Maven Plugin**: Use `mvn spring-boot:build-image` to generate OCI images with default Paketo Buildpacks.
- **Actuator Endpoints**: Enable `/actuator/health`, `/actuator/metrics` for monitoring.
- **CI/CD**: Add `--batch-mode -Pnative` in Maven workflow (if GraalVM Native Image compilation is needed).

## 🛡️ Exception Handling Standards

### Custom Exception Structure

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    private final String errorCode;

    public UserNotFoundException(String message) {
        super(message);
        this.errorCode = "USER_NOT_FOUND";
    }

    public UserNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
```

### Global Exception Handler

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFound(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(404, ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(ValidationException ex) {
        log.warn("Validation failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(400, "Validation failed", ex.getFieldErrors()));
    }
}
```

---

## 🚀 Performance Optimization Best Practices

### Database Query Optimization

```java
// 使用分頁查詢
@Query("SELECT u FROM UserEntity u WHERE u.status = :status")
Page<UserEntity> findByStatus(@Param("status") String status, Pageable pageable);

// 使用 @EntityGraph 避免 N+1 問題
@EntityGraph(attributePaths = {"department", "roles"})
@Query("SELECT u FROM UserEntity u WHERE u.id = :id")
Optional<UserEntity> findByIdWithDetails(@Param("id") Long id);

// 使用投影 (Projection) 減少資料傳輸
@Query("SELECT new com.systex.course.dto.UserSummaryDto(u.id, u.username, u.email) " +
       "FROM UserEntity u WHERE u.departmentId = :deptId")
List<UserSummaryDto> findUserSummaryByDepartment(@Param("deptId") Long deptId);
```

### Caching Strategy

```java
@Service
@CacheConfig(cacheNames = "users")
public class UserService {

    @Cacheable(key = "#id")
    public UserEntity findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    @CacheEvict(key = "#user.id")
    public UserEntity update(UserEntity user) {
        return userRepository.save(user);
    }

    @CacheEvict(allEntries = true)
    public void clearCache() {
        // Clear all user cache
    }
}
```

---

## 🔐 Security Best Practices

- Enforce Bean Validation on all request bodies.
- Never log sensitive data (passwords, tokens).
- Encode passwords with `BCryptPasswordEncoder` (strength = 10).

### Input Validation

```java
@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    @PostMapping
    public ResponseEntity<ApiResponse<UserVo>> createUser(
            @Valid @RequestBody UserCreateDto userDto) {
        // Spring Boot automatically validates @Valid annotated parameters
        UserVo user = userService.createUser(userDto);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
```

### Password Security

```java
@Service
public class PasswordService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
```

---

## 📊 Monitoring & Logging

### Logging Configuration

- One SLF4J logger per class; use placeholders `{}`.
- Levels: ERROR, WARN, INFO, DEBUG, TRACE.
- Guard debug logs with `log.isDebugEnabled()`.

```java
@Slf4j
@Service
public class UserService {

    public UserEntity createUser(UserCreateDto userDto) {
        log.info("Starting user creation: {}", userDto.getUsername());

        try {
            UserEntity user = convertToEntity(userDto);
            UserEntity savedUser = userRepository.save(user);

            log.info("User created successfully: ID={}, Username={}",
                savedUser.getId(), savedUser.getUsername());

            return savedUser;
        } catch (Exception e) {
            log.error("User creation failed: Username={}, Error={}",
                userDto.getUsername(), e.getMessage(), e);
            throw e;
        }
    }
}
```

### Actuator Endpoint Configuration

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: when-authorized
```

---

## 🛠️ Development Tools & Configuration

### Recommended Maven Dependencies

```xml
<!-- Spring Boot Starters -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Development Tools -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### IDE Configuration Recommendations

- **Enable Annotation Processing**: Support for Lombok
- **Code Formatting**: Use Google Java Style
- **Static Analysis**: Integrate SonarLint
- **Test Execution**: Support JUnit 5

---

## 🚨 Common Pitfalls & Avoidance Methods

1. **N+1 Query Issues**: Use `@EntityGraph` or `JOIN FETCH`
2. **Lazy Loading Exceptions**: Complete related data access within transactions
3. **Memory Leaks**: Properly close resources, use try-with-resources
4. **Circular Dependencies**: Redesign class relationships, use interfaces for decoupling
5. **Transaction Failures**: Ensure transactional methods are called on Spring-managed beans
6. **SQL Injection**: Use parameterized queries, avoid string concatenation
7. **Sensitive Information Leakage**: Use `@JsonIgnore` to hide sensitive fields

---

## ⚠️ Upgrade / Compatibility Checklist

1. Remove all `javax.*` imports → replace with `jakarta.*`.
2. Ensure third-party libraries are Spring Boot 3.x compatible.
3. Update Maven plugins that fail on Java 21.

---

## ✅ Commit Gate Requirements

- Build must pass `mvn verify -B`.
- All tests green and coverage ≥ 80%.
- Lint with `maven-checkstyle-plugin`.

### 設計原則

- [ ] 是否遵循單一職責原則
- [ ] 是否正確分離了關注點
- [ ] 是否適當使用了設計模式

### 代碼品質

- [ ] 變數和方法命名是否清晰
- [ ] 是否有適當的註釋和文檔
- [ ] 是否遵循了編碼規範

### 安全性

- [ ] 是否進行了輸入驗證
- [ ] 是否處理了潛在的安全漏洞
- [ ] 是否正確處理了敏感資訊

### 性能

- [ ] 是否存在潛在的性能問題
- [ ] 是否適當使用了快取
- [ ] 是否優化了資料庫查詢

### 測試

- [ ] 是否有足夠的單元測試
- [ ] 測試是否覆蓋了邊界條件
- [ ] 是否有整合測試

---

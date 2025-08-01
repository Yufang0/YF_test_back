---
applyTo: "**/*.{java,kt}"
---

# ERP Back‑End • Spring Boot 3.5.4 Coding Rules

> Follow every rule exactly. Do not suggest alternatives unless asked. Assume Java 21 + Maven 3.9.11 + Spring Boot 3.5.4.

---

## 📦 Build & Dependency Rules
- **Parent POM**: `org.springframework.boot:spring-boot-starter-parent:3.5.4`.
- **Java**: set `<java.version>21</java.version>` **and** `<maven.compiler.release>21</maven.compiler.release>`.
- **Mandatory starters**: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, `spring-boot-starter-security`, `spring-boot-starter-test` (scope test).
- **OpenAPI**: add `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9`.
- Use Jakarta EE 10 dependencies only; never import `javax.*`.
- Enable container images with the Maven goal `spring-boot:build-image`.

---

## 🔧 Configuration Defaults
- Application config file: `application.yml`.
- Enable virtual threads: `spring.threads.virtual.enabled=true`.
- JPA: `spring.jpa.hibernate.ddl-auto=validate` by default; avoid `create`/`update` in production.
- Expose OpenAPI on actuator port: `springdoc.show-actuator=true` if management port differs.

---

## 5. 開發流程建議
### A. 模型與資料持久化
- 使用 JPA + Hibernate（或 Spring Data JPA）定義 Entity / Repository，配合 Bean Validation（JSR‑303）進行資料驗證。
- 善用 Spring Boot 的屬性 binding 自動載入 application.properties 文件設定。

### B. REST 控制器層
- 採用 `@RestController` + DTO 進行資料交換。
- 使用 `@Validated`, `@NotNull`, `@Size` 等註解進行輸入驗證。

### C. 錯誤處理與例外
- 使用 `@ControllerAdvice` 處理全域例外，返回統一的錯誤格式。

### D. 安全與權限控制
- 使用 Spring Security，搭配 OAuth2 資訊或 Basic 認證（視需求），控制 API 存取。
- 若使用 Spring Security OAuth2 client/server，確保 springdoc-openapi 適當忽略 `@AuthenticationPrincipal` 提供的 endpoint。

### E. 測試
- 編寫單元測試與整合測試，使用 Spring Boot 的測試 starter：`spring-boot-starter-test`。
- 如需 Native Image 測試，參考官方 native‑image 測試文檔。

---

## 🏗️ Project Structure
### 核心業務層 (Core Business Layers)
```
com.systex.course/
├── entity/          # JPA 實體類別 (XxxEntity)
├── dto/            # 數據傳輸物件 (XxxDto)
├── vo/             # 視圖物件 (XxxVo)
├── repo/           # 資料存取層 (XxxRepository)
├── service/        # 業務邏輯層 (XxxService)
└── controller/     # 控制器層 (XxxController)
```

### 基礎設施層 (Infrastructure Layers)
```
com.systex.course/
├── config/         # 配置類別 (XxxConfig)
├── exception/      # 異常處理 (XxxException)
├── constant/       # 常數與枚舉 (XxxEnum/XxxType)
├── util/          # 工具類別 (XxxUtil/XxxHelper)
└── system/        # 系統級元件
    ├── filter/    # 過濾器 (XxxFilter)
    ├── interceptor/ # 攔截器 (XxxInterceptor)
    ├── aspect/    # 切面 (XxxAspect)
    └── handler/   # 處理器 (XxxHandler)
```

### 命名規範詳細說明
- **Entity**: 實體類別命名為 `XxxEntity`，對應資料表
- **DTO**: 資料傳輸物件命名為 `XxxDto`，用於 API 請求/回應
- **VO**: 視圖物件命名為 `XxxVo`，用於前端展示
- **Repository**: 資料存取介面命名為 `XxxRepository`
- **Service**: 業務邏輯類別命名為 `XxxService`
- **Controller**: 控制器類別命名為 `XxxController`


### 統一回應格式
所有 API 回應應遵循標準格式：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... },         // 實際資料
  "timestamp": "2025-01-15T10:00:00Z",
  "path": "/api/v1/users"
}
```

### 分頁回應格式
```json
{
  "code": 200,
  "message": "查詢成功",
  "data": {
    "content": [...],      // 當前頁資料
    "page": 0,            // 當前頁碼 (0-based)
    "size": 20,           // 每頁大小
    "totalElements": 100,  // 總記錄數
    "totalPages": 5,      // 總頁數
    "hasNext": true,      // 是否有下一頁
    "hasPrevious": false  // 是否有上一頁
  }
}
```

---

## ✍️ Coding Conventions
- Use **constructor injection** only; no `@Autowired` on fields.
- Use `record` classes for immutable DTOs where appropriate.
- Validate input with Jakarta Bean Validation (`@NotNull`, `@Size`…).
- Global error handling via `@ControllerAdvice` + `@ExceptionHandler` returning RFC 7807 Problem Details JSON.
- Security: Spring Security 6; prefer JWT Bearer auth for APIs, HTTP Basic only for internal endpoints.

---

## 🗄️ 資料庫設計規範

### 命名規則
- **資料表**: 小寫 + 底線分隔 (`snake_case`)，例如：`user_profile`
- **主鍵**: 統一命名為 `id`
- **外鍵**: 格式為 `{referenced_table}_id`，例如：`user_id`
- **索引**: 格式為 `idx_{table}_{column(s)}`
- **約束**: 格式為 `{constraint_type}_{table}_{column}`

### JPA 實體最佳實踐
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

## 📐 DTO/VO 設計規範

### DTO 規範 (API 層面)
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
    @Email(message = "郵箱格式不正確")
    private String email;
    
    @JsonProperty("password")
    @NotBlank(message = "密碼不能為空")
    @Size(min = 6, message = "密碼長度至少6位")
    private String password;
}
```

### VO 規範 (展示層面)
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

## 🧪 Testing
- Use JUnit 5 (`@SpringBootTest(webEnvironment = RANDOM_PORT)`).
- Integrate Testcontainers for database integration tests.
- Use Mockito/AssertJ; avoid PowerMock.
- 編寫單元測試與整合測試，使用 Spring Boot 的測試 starter：`spring-boot-starter-test`。
- 如需 Native Image 測試，參考官方 native‑image 測試文檔。

### 測試類別結構
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    @DisplayName("根據ID查詢用戶 - 用戶存在時應返回用戶信息")
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
    @DisplayName("根據ID查詢用戶 - 用戶不存在時應拋出異常")
    void testFindUserById_WhenUserNotExists_ShouldThrowException() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThatThrownBy(() -> userService.findById(userId))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage("用戶不存在: " + userId);
    }
}
```

### 測試命名規則
- **測試類別**: `{被測試類別名稱}Test`
- **測試方法**: `test{方法名稱}_{測試條件}_{預期結果}`
- **顯示名稱**: 使用 `@DisplayName` 提供中文描述

### 測試覆蓋率目標
- **方法覆蓋率**: >= 80%
- **分支覆蓋率**: >= 70%
- **行覆蓋率**: >= 75%

---

## 🚀 Deployment & Observability
- Health/metrics via Spring Boot Actuator (`/actuator/health`, `/actuator/metrics`).
- Package as OCI image with Buildpacks (`paketobuildpacks/builder-jammy-base`).
- Add Prometheus & OpenTelemetry exporters only when needed; keep disabled by default.
- Maven Plugin：mvn spring-boot:build-image 產生 OCI 映像，預設使用 Paketo Buildpacks。
- Actuator：開啟 /actuator/health、/actuator/metrics 供監控。
- CI/CD：在 Maven workflow 中加入 --batch-mode -Pnative（如需 GraalVM Native Image 編譯）。


## 🛡️ 異常處理規範

### 自訂異常結構
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

### 全域異常處理器
```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFound(UserNotFoundException ex) {
        log.warn("用戶不存在: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(404, ex.getMessage()));
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(ValidationException ex) {
        log.warn("參數驗證失敗: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(400, "參數驗證失敗", ex.getFieldErrors()));
    }
}
```

---

## 🚀 性能優化最佳實踐

### 資料庫查詢優化
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

### 快取策略
```java
@Service
@CacheConfig(cacheNames = "users")
public class UserService {
    
    @Cacheable(key = "#id")
    public UserEntity findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("用戶不存在: " + id));
    }
    
    @CacheEvict(key = "#user.id")
    public UserEntity update(UserEntity user) {
        return userRepository.save(user);
    }
    
    @CacheEvict(allEntries = true)
    public void clearCache() {
        // 清除所有用戶快取
    }
}
```

---

## 🔐 安全性最佳實踐

### 輸入驗證
```java
@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {
    
    @PostMapping
    public ResponseEntity<ApiResponse<UserVo>> createUser(
            @Valid @RequestBody UserCreateDto userDto) {
        // Spring Boot 會自動驗證 @Valid 標記的參數
        UserVo user = userService.createUser(userDto);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
```

### 密碼安全
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

## 📊 監控與日誌

### 日誌配置
```java
@Slf4j
@Service
public class UserService {
    
    public UserEntity createUser(UserCreateDto userDto) {
        log.info("開始創建用戶: {}", userDto.getUsername());
        
        try {
            UserEntity user = convertToEntity(userDto);
            UserEntity savedUser = userRepository.save(user);
            
            log.info("用戶創建成功: ID={}, Username={}", 
                savedUser.getId(), savedUser.getUsername());
            
            return savedUser;
        } catch (Exception e) {
            log.error("用戶創建失敗: Username={}, Error={}", 
                userDto.getUsername(), e.getMessage(), e);
            throw e;
        }
    }
}
```

### Actuator 端點配置
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

## 🛠️ 開發工具與配置

### 推薦的 Maven 依賴
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

### IDE 配置建議
- **啟用註解處理**: 支援 Lombok
- **代碼格式化**: 使用 Google Java Style
- **靜態分析**: 集成 SonarLint
- **測試運行**: 支援 JUnit 5

---

## 🚨 常見陷阱與避免方法

1. **N+1 查詢問題**: 使用 `@EntityGraph` 或 `JOIN FETCH`
2. **懶載入異常**: 在事務內完成關聯資料存取
3. **記憶體洩漏**: 正確關閉資源，使用 try-with-resources
4. **循環依賴**: 重新設計類別關係，使用介面解耦
5. **事務失效**: 確保在 Spring 管理的 Bean 中調用事務方法
6. **SQL 注入**: 使用參數化查詢，避免字串拼接
7. **敏感資訊洩漏**: 使用 `@JsonIgnore` 隱藏敏感欄位

---

## ⚠️ Upgrade / Compatibility Checklist
1. Remove all `javax.*` imports → replace with `jakarta.*`.
2. Ensure third‑party libs are Spring Boot 3.x compatible.
3. Update Maven plugins that fail on Java 21.

---

## ✅ Commit Gate
- Build must pass `mvn verify -B`.
- All tests green and coverage ≥ 80 %.
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

````
---


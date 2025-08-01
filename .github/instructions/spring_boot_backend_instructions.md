---
applyTo: "**/*.{java,kt}"
---

# ERP Back‑End • Spring Boot 3.5.4 Coding Rules

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
- 編寫單元測試與整合測試，使用 Spring Boot 的測試 starter：`spring-boot-starter-test`。
- 如需 Native Image 測試，參考官方 native‑image 測試文檔。

## 🚀 Deployment & Observability
- Health/metrics via Spring Boot Actuator (`/actuator/health`, `/actuator/metrics`).
- Package as OCI image with Buildpacks (`paketobuildpacks/builder-jammy-base`).
- Add Prometheus & OpenTelemetry exporters only when needed; keep disabled by default.
- Maven Plugin：mvn spring-boot:build-image 產生 OCI 映像，預設使用 Paketo Buildpacks。
- Actuator：開啟 /actuator/health、/actuator/metrics 供監控。
- CI/CD：在 Maven workflow 中加入 --batch-mode -Pnative（如需 GraalVM Native Image 編譯）。

## ⚠️ Upgrade / Compatibility Checklist
1. Remove all `javax.*` imports → replace with `jakarta.*`.
2. Ensure third‑party libs are Spring Boot 3.x compatible.
3. Update Maven plugins that fail on Java 21.

## ✅ Commit Gate
- Build must pass `mvn verify -B`.
- All tests green and coverage ≥ 80 %.
- Lint with `maven-checkstyle-plugin`.

---


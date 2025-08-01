# Spring Boot 3.5.4 ERP 後端開發指南

## Updated Spring Boot 3.5.4 ERP Back-End Instructions Prompt

### 1. 技術基礎與版本

* **Java 21 LTS**：採用最新 LTS 特性（record patterns、switch 增強、virtual threads 等）；Spring Boot 3.x 官方已完全支援 Java 21。
* **Apache Maven 3.9.11**：符合 Java 8+ 要求，官方最新穩定版。
* **Spring Boot 3.5.4**：最新 GA，修復 53 個 bug 並升級依賴。
* **Jakarta EE 10 命名空間**：所有 javax.\* API 已遷移至 jakarta.\*，JPA 使用 Jakarta Persistence 3.2。

### 2. 專案骨架與依賴

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>3.5.4</version>
</parent>

<properties>
  <java.version>21</java.version>
  <maven.compiler.release>21</maven.compiler.release>
</properties>

<dependencies>
  <!-- Web / Validation / JPA -->
  <dependency> <groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId></dependency>
  <dependency> <groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-data-jpa</artifactId></dependency>
  <dependency> <groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-validation</artifactId></dependency>

  <!-- Security -->
  <dependency> <groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-security</artifactId></dependency>

  <!-- OpenAPI -->
  <dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.9</version>
  </dependency>
</dependencies>
```

springdoc 2.8.9 為目前最新版，與 Spring Boot 3.5 完整相容。

### 3. 關鍵設定

| 功能                       | application.yml 範例                                                               |
| ------------------------ | -------------------------------------------------------------------------------- |
| Virtual Threads          | `spring.threads.virtual.enabled=true`（Java 21+）                   |
| OpenAPI on Actuator Port | `springdoc.show-actuator=true`（若使用獨立管理埠） |
| JPA DDL                  | `spring.jpa.hibernate.ddl-auto=validate`（生產環境禁用 create / update  |

### 4. 分層設計

* **Entity / Repository**：使用 Jakarta Persistence 3.2 註解；Repository 介面繼承 `JpaRepository`。
* **DTO + Mapper**：控制器層僅暴露 DTO，使用 MapStruct 或 record for projection（Java 21 支援）。
* **Service**：業務邏輯，採 constructor-based DI，避免 field injection。
* **Controller**：`@RestController` + `@Validated`；錯誤由 `@ControllerAdvice` 統一處理。

### 5. 安全 / 驗證

* Spring Security 6（隨 Boot 3）支援 OAuth2 Client / Resource Server；未登入請回傳 HTTP 401，權限不足回傳 403。
* 若僅內網使用，可用 HTTP Basic；對外服務建議 JWT + OAuth2。

### 5. 開發流程建議

#### A. 模型與資料持久化

* 使用 JPA + Hibernate（或 Spring Data JPA）定義 Entity / Repository，配合 Bean Validation（JSR‑303）進行資料驗證。
* 善用 Spring Boot 的屬性 binding 自動載入 application.properties 文件設定。

#### B. REST 控制器層

* 採用 `@RestController` + DTO 進行資料交換。
* 使用 `@Validated`, `@NotNull`, `@Size` 等註解進行輸入驗證。

#### C. 錯誤處理與例外

* 使用 `@ControllerAdvice` 處理全域例外，返回統一的錯誤格式。

#### D. 安全與權限控制

* 使用 Spring Security，搭配 OAuth2 資訊或 Basic 認證（視需求），控制 API 存取。
* 若使用 Spring Security OAuth2 client/server，確保 springdoc-openapi 適當忽略 `@AuthenticationPrincipal` 提供的 endpoint。

#### E. 測試

* 編寫單元測試與整合測試，使用 Spring Boot 的測試 starter：`spring-boot-starter-test`。
* 如需 Native Image 測試，參考官方 native‑image 測試文檔。

### 6. 測試

* `spring-boot-starter-test`（JUnit 5、AssertJ、Mockito）。
* 使用 `@SpringBootTest(webEnvironment = RANDOM_PORT)` 做整合測試，並以 Testcontainers 模擬資料庫。

### 7. 建置與部署

* **Maven Plugin**：`mvn spring-boot:build-image` 產生 OCI 映像，預設使用 Paketo Buildpacks。
* **Actuator**：開啟 `/actuator/health`、`/actuator/metrics` 供監控。
* **CI/CD**：在 Maven workflow 中加入 `--batch-mode -Pnative`（如需 GraalVM Native Image 編譯）。

### 8. 升級注意事項

1. 移除所有 javax.\* import，改用 jakarta.\*。
2. 第三方庫若未支援 Spring Boot 3 或 Jakarta EE 10，需升級或替換。
3. Maven 早期版的 Shade/Checkstyle plugin 若不支援 Java 21，請升級到最新 release。

---
---
applyTo: "**/*.java"
---

# 🌱 Spring Boot 3.5.4 開發規範 for GitHub Copilot

## 🔧 基礎規則

- 使用 Java 21 或更新版本
- 使用 Spring Boot 3.5.4 與 Jakarta EE (jakarta.\*) annotations
- 使用 Lombok 簡化程式碼，例如 `@Data`, `@Builder`, `@RequiredArgsConstructor`
- 使用 constructor-based dependency injection
- 禁止使用 field injection
- 禁止使用 `System.out.println` 進行輸出，應使用專門的日誌框架

### Virtual Threads

- 若要啟用虛擬執行緒，請在 `application.yml` 新增  
  `spring.threads.virtual.enabled=true`。

## 🛠️ 建置與部署

- 產生 OCI 映像：`mvn spring-boot:build-image`（Spring Boot Maven Plugin 內建 Buildpacks 支援）。

## 📝 命名規範

- 使用 **PascalCase** 命名類別、介面和類型別名
- 使用 **camelCase** 命名變數、函數和方法
- 使用 **全大寫蛇形命名法(SNAKE_CASE)** 命名常數，例如：`MAX_RETRY_COUNT`
- 避免使用匈牙利命名法（如 `strName`、`iCount`）
- 方法名應以動詞開頭，例如：`getUserById`, `saveOrder`
- Boolean 變數和方法應以 `is`, `has`, `can`, `should` 等開頭，例如：`isActive`, `hasPermission`

## 🧩 程式碼格式化

- 縮排使用 4 個空格（非 Tab 字符）
- 行寬限制為 120 字符
- 方法之間用一行空白分隔
- 相關的程式碼應分組在一起，並使用空行分隔不同的邏輯區塊
- 大括號應遵循 K&R 風格（左括號不換行，右括號獨立一行）
- 每行代碼應僅包含一條語句

## 🛡️ 異常處理

- 捕獲具體的異常，而非籠統的 `Exception`
- 不要捕獲異常後不處理或僅打印堆疊追蹤
- 自定義異常類別應提供有意義的錯誤訊息和相關資訊
- 避免在 catch 塊中使用 `e.printStackTrace()`，應使用日誌框架記錄異常
- 業務邏輯異常應使用自定義異常類，並提供清晰的錯誤碼和訊息

## 📚 註解使用

- 類別和公開方法必須有 JavaDoc 註解
- 複雜的私有方法應有註解說明其用途和實現邏輯
- JavaDoc 應包含參數說明 `@param`、返回值說明 `@return` 和拋出異常說明 `@throws`
- 避免無意義的註解，如 `// 建構子` 或 `// getter/setter`
- 使用 `@Deprecated` 標記已棄用的方法，並說明替代方案

## 🔍 程式碼品質

- 遵循 SOLID 原則
- 遵循 DRY (Don't Repeat Yourself) 原則，避免代碼重複
- 類別應保持單一職責（SRP）
- 方法應盡可能短小，通常不超過 50 行
- 避免過深的嵌套（不超過 3 層）
- 優先使用不可變（immutable）物件
- 避免魔法數字和字串，使用有意義的常數
- 使用 Optional 代替返回 null

## 📊 日誌使用

- 每個 Class 都實作使用 SLF4J API 進行日誌記錄
- 適當使用不同的日誌級別（ERROR, WARN, INFO, DEBUG, TRACE）
- 日誌訊息應該提供足夠的上下文資訊
- 敏感信息（密碼、身份證號等）不得記錄到日誌
- 使用佔位符 `{}` 而非字串拼接，例如：`log.info("User {} logged in", username)`
- 在 DEBUG 級別日誌使用前先檢查是否啟用，例如：`if (log.isDebugEnabled()) { ... }`

## 🧪 測試實踐 - 通用原則

- 遵循 AAA (Arrange-Act-Assert) 模式編寫單元測試
- 每個試方法僅測試一個行為或功能點
- 測試名稱應描述被測試的行為和預期結果
- 使用 Mockito, JUnit 5 和 AssertJ 等測試工具
- 避免在單元測試中使用真實的外部依賴（如資料庫、檔案系統）
- 為邊界條件和錯誤情境編寫測試案例

## 📄 API 文件

- 產生 Swagger / OpenAPI：應採用 `springdoc-openapi-starter-webmvc-ui:2.8.9`（與 Spring Boot 3.5.4 相容）。

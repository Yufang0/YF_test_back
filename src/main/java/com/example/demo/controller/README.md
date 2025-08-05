# Controller 資料夾

## 概述

此資料夾包含所有控制器層 (Controller Layer) 的類別。Controller 負責處理 HTTP 請求、調用 Service 層的業務邏輯、以及返回統一格式的 HTTP 回應。Controller 是 Web API 的入口點，定義了所有對外的 REST 端點。

## 資料夾內容

- **EmployeeController.java** - 員工控制器，提供員工相關的 REST API 端點

## 重要慣例

### 命名規範

- 所有 Controller 類別以 `Controller` 結尾
- 類別名稱使用 PascalCase
- 方法名稱使用動詞，清楚表達 API 操作
- URL 路徑使用 kebab-case（小寫加連字號）

### 註解使用

- `@RestController` - 標記為 REST 控制器
- `@RequestMapping` - 定義基礎 URL 路徑
- `@RequiredArgsConstructor` - Lombok 建構函式注入
- `@Validated` - 啟用參數驗證
- `@Tag` - Swagger 文件標籤

### HTTP 方法對應

- `@GetMapping` - 查詢操作
- `@PostMapping` - 建立操作
- `@PutMapping` - 更新操作
- `@DeleteMapping` - 刪除操作

## 快速開始

### 建立新的 Controller

```java
@RestController
@RequestMapping("/api/v1/examples")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Example Management", description = "範例管理相關 API")
public class ExampleController {

    private final ExampleService exampleService;

    @PostMapping
    @Operation(summary = "建立範例", description = "建立新的範例資料")
    public ResponseEntity<ApiResponse<ExampleVo>> createExample(
            @Valid @RequestBody ExampleCreateDto createDto) {

        log.info("Creating example: name={}", createDto.getName());

        ExampleVo result = exampleService.createExample(createDto);
        ApiResponse<ExampleVo> response = ApiResponse.success(result, "建立成功");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查詢範例", description = "根據 ID 查詢範例資料")
    public ResponseEntity<ApiResponse<ExampleVo>> getExampleById(
            @PathVariable @Min(value = 1, message = "ID 必須大於 0") Integer id) {

        ExampleVo result = exampleService.getExampleById(id);
        ApiResponse<ExampleVo> response = ApiResponse.success(result);

        return ResponseEntity.ok(response);
    }
}
```

## 最小工作範例

```java
/**
 * 員工控制器
 * 提供員工相關的 REST API 端點
 */
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Employee Management", description = "員工管理相關 API")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * 建立新員工
     */
    @PostMapping
    @Operation(summary = "建立員工", description = "建立新的員工資料")
    public ResponseEntity<ApiResponse<EmployeeVo>> createEmployee(
            @Valid @RequestBody EmployeeCreateDto createDto) {

        log.info("Creating employee: empCode={}", createDto.getEmpCode());

        EmployeeVo result = employeeService.createEmployee(createDto);
        ApiResponse<EmployeeVo> response = ApiResponse.success(result, "員工建立成功");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 根據 ID 查詢員工
     */
    @GetMapping("/{id}")
    @Operation(summary = "查詢員工", description = "根據員工 ID 查詢員工資訊")
    public ResponseEntity<ApiResponse<EmployeeVo>> getEmployeeById(
            @PathVariable @Min(value = 1, message = "員工 ID 必須大於 0") Integer id) {

        EmployeeVo result = employeeService.getEmployeeById(id);
        ApiResponse<EmployeeVo> response = ApiResponse.success(result);

        return ResponseEntity.ok(response);
    }
}
```

## REST API 設計原則

### 1. URL 設計

```java
@RequestMapping("/api/v1/employees")  // 基礎路徑
@GetMapping                          // GET /api/v1/employees
@GetMapping("/{id}")                 // GET /api/v1/employees/{id}
@PostMapping                         // POST /api/v1/employees
@PutMapping("/{id}")                 // PUT /api/v1/employees/{id}
@DeleteMapping("/{id}")              // DELETE /api/v1/employees/{id}
```

### 2. HTTP 狀態碼使用

- `200 OK` - 查詢、更新成功
- `201 Created` - 建立成功
- `400 Bad Request` - 請求參數錯誤
- `404 Not Found` - 資源不存在
- `409 Conflict` - 資源衝突（如重複建立）
- `500 Internal Server Error` - 伺服器內部錯誤

### 3. 統一回應格式

```java
// 成功回應
return ResponseEntity.ok(ApiResponse.success(data));

// 建立成功回應
return ResponseEntity.status(HttpStatus.CREATED)
    .body(ApiResponse.success(data, "建立成功"));
```

## 參數驗證

### 1. 請求體驗證

```java
public ResponseEntity<ApiResponse<EmployeeVo>> createEmployee(
        @Valid @RequestBody EmployeeCreateDto createDto) {
    // Spring 會自動驗證 createDto 中的註解
}
```

### 2. 路徑參數驗證

```java
public ResponseEntity<ApiResponse<EmployeeVo>> getEmployeeById(
        @PathVariable @Min(value = 1, message = "員工 ID 必須大於 0") Integer id) {
    // 驗證路徑參數
}
```

### 3. 查詢參數驗證

```java
public ResponseEntity<ApiResponse<PageResponse<EmployeeVo>>> getAllEmployees(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "頁碼不能小於 0") Integer page,
        @RequestParam(defaultValue = "20") @Min(value = 1, message = "每頁大小不能小於 1") Integer size) {
    // 查詢參數驗證
}
```

## 分頁查詢處理

```java
@GetMapping
public ResponseEntity<ApiResponse<PageResponse<EmployeeVo>>> getAllEmployees(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "createTime") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("desc") ?
        Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<EmployeeVo> result = employeeService.getAllEmployees(pageable);
    PageResponse<EmployeeVo> pageResponse = PageResponse.from(result);
    ApiResponse<PageResponse<EmployeeVo>> response = ApiResponse.success(pageResponse);

    return ResponseEntity.ok(response);
}
```

## Swagger 文件註解

### 1. 控制器層級

```java
@Tag(name = "Employee Management", description = "員工管理相關 API")
public class EmployeeController {
}
```

### 2. 方法層級

```java
@Operation(summary = "建立員工", description = "建立新的員工資料")
@ApiResponses(value = {
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "201", description = "員工建立成功"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400", description = "請求參數錯誤"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "409", description = "員工編號已存在")
})
```

### 3. 參數層級

```java
@Parameter(description = "員工 ID", required = true)
@PathVariable Integer id
```

## 異常處理

Controller 不直接處理異常，異常由 `GlobalExceptionHandler` 統一處理：

```java
// 在 Controller 中正常拋出異常
if (id <= 0) {
    throw new IllegalArgumentException("員工 ID 必須大於 0");
}

// GlobalExceptionHandler 會自動捕獲並返回統一格式的錯誤回應
```

## 日誌記錄

```java
@PostMapping
public ResponseEntity<ApiResponse<EmployeeVo>> createEmployee(
        @Valid @RequestBody EmployeeCreateDto createDto) {

    log.info("Creating employee: empCode={}", createDto.getEmpCode());

    EmployeeVo result = employeeService.createEmployee(createDto);

    // 成功不需要記錄日誌，Service 層已記錄
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(result, "員工建立成功"));
}
```

## 注意事項

1. Controller 只負責 HTTP 請求處理，不包含業務邏輯
2. 使用 `@Valid` 驗證請求資料
3. 使用統一的 `ApiResponse` 格式回應
4. 適當設定 HTTP 狀態碼
5. 提供完整的 Swagger 文件註解
6. 記錄關鍵操作的日誌
7. 路徑參數和查詢參數要進行驗證
8. 分頁查詢要提供排序功能
9. URL 設計要符合 RESTful 原則
10. 每個方法都要有完整的 JavaDoc 註釋

# Exception 資料夾

## 概述

此資料夾包含所有自定義異常類別和全域異常處理器。異常處理是應用程式健壯性的重要組成部分，提供統一的錯誤回應格式和適當的 HTTP 狀態碼。

## 資料夾內容

- **EmployeeNotFoundException.java** - 員工未找到異常
- **EmployeeAlreadyExistsException.java** - 員工已存在異常
- **GlobalExceptionHandler.java** - 全域異常處理器

## 重要慣例

### 命名規範

- 異常類別以 `Exception` 結尾
- 類別名稱使用 PascalCase
- 異常名稱要清楚表達錯誤類型
- 全域處理器命名為 `GlobalExceptionHandler`

### 註解使用

- `@ResponseStatus` - 指定 HTTP 狀態碼
- `@RestControllerAdvice` - 全域異常處理器
- `@ExceptionHandler` - 異常處理方法

### 異常層次結構

- 繼承適當的 Java 異常類別
- 業務異常通常繼承 `RuntimeException`
- 系統異常繼承對應的檢查異常

## 快速開始

### 建立新的業務異常

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExampleNotFoundException extends RuntimeException {

    private final String errorCode;

    public ExampleNotFoundException(String message) {
        super(message);
        this.errorCode = "EXAMPLE_NOT_FOUND";
    }

    public ExampleNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
```

### 在全域處理器中處理異常

```java
@ExceptionHandler(ExampleNotFoundException.class)
public ResponseEntity<ApiResponse<?>> handleExampleNotFound(
        ExampleNotFoundException ex, WebRequest request) {
    log.warn("Example not found: {}", ex.getMessage());

    ApiResponse<?> response = ApiResponse.error(404, ex.getMessage());
    response.setPath(request.getDescription(false).replace("uri=", ""));

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
}
```

## 最小工作範例

```java
/**
 * 員工未找到異常
 * 當根據指定條件無法找到員工時拋出此例外
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends RuntimeException {

    private final String errorCode;

    public EmployeeNotFoundException(String message) {
        super(message);
        this.errorCode = "EMPLOYEE_NOT_FOUND";
    }

    public String getErrorCode() {
        return errorCode;
    }
}

/**
 * 全域異常處理器
 * 統一處理應用程式中的各種例外
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleEmployeeNotFound(
            EmployeeNotFoundException ex, WebRequest request) {
        log.warn("Employee not found: {}", ex.getMessage());

        ApiResponse<?> response = ApiResponse.error(404, ex.getMessage());
        response.setPath(request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
```

## 異常類型分類

### 1. 業務異常

- **NotFound 異常** - 資源不存在
- **AlreadyExists 異常** - 資源已存在
- **InvalidOperation 異常** - 無效操作
- **BusinessRule 異常** - 業務規則違反

### 2. 驗證異常

- **ValidationException** - 資料驗證失敗
- **ConstraintViolationException** - 約束違反
- **MethodArgumentNotValidException** - 方法參數驗證失敗

### 3. 系統異常

- **RuntimeException** - 執行時期異常
- **Exception** - 一般異常

## 常見異常處理模式

### 1. 資源不存在異常

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, Object id) {
        super(String.format("%s not found: %s", resourceType, id));
    }
}

// 使用方式
throw new ResourceNotFoundException("Employee", id);
```

### 2. 資源衝突異常

```java
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceConflictException extends RuntimeException {
    public ResourceConflictException(String message) {
        super(message);
    }
}

// 使用方式
throw new ResourceConflictException("員工編號已存在: " + empCode);
```

### 3. 業務規則異常

```java
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String rule, String violation) {
        super(String.format("Business rule violation - %s: %s", rule, violation));
    }
}
```

## 全域異常處理器設計

### 1. 異常處理方法結構

```java
@ExceptionHandler(SpecificException.class)
public ResponseEntity<ApiResponse<?>> handleSpecificException(
        SpecificException ex, WebRequest request) {

    // 1. 記錄日誌
    log.warn("Exception occurred: {}", ex.getMessage());

    // 2. 建立回應
    ApiResponse<?> response = ApiResponse.error(statusCode, ex.getMessage());
    response.setPath(request.getDescription(false).replace("uri=", ""));

    // 3. 返回回應
    return ResponseEntity.status(HttpStatus.STATUS_CODE).body(response);
}
```

### 2. 驗證異常處理

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ApiResponse<?>> handleValidationErrors(
        MethodArgumentNotValidException ex, WebRequest request) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
    });

    ApiResponse<?> response = ApiResponse.error(400, "資料驗證失敗", errors);
    response.setPath(request.getDescription(false).replace("uri=", ""));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
}
```

### 3. 通用異常處理

```java
@ExceptionHandler(Exception.class)
public ResponseEntity<ApiResponse<?>> handleGenericException(
        Exception ex, WebRequest request) {

    log.error("Unexpected exception: {}", ex.getMessage(), ex);

    ApiResponse<?> response = ApiResponse.error(500, "系統發生未預期的錯誤");
    response.setPath(request.getDescription(false).replace("uri=", ""));

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
}
```

## HTTP 狀態碼對應

| 狀態碼 | 異常類型              | 使用場景               |
| ------ | --------------------- | ---------------------- |
| 400    | Bad Request           | 請求參數錯誤、驗證失敗 |
| 401    | Unauthorized          | 未授權存取             |
| 403    | Forbidden             | 權限不足               |
| 404    | Not Found             | 資源不存在             |
| 409    | Conflict              | 資源衝突、重複建立     |
| 422    | Unprocessable Entity  | 業務規則違反           |
| 500    | Internal Server Error | 系統內部錯誤           |

## 錯誤回應格式

### 1. 基本錯誤回應

```json
{
  "code": 404,
  "message": "員工不存在: 123",
  "data": null,
  "timestamp": "2025-01-15T10:00:00Z",
  "path": "/api/v1/employees/123"
}
```

### 2. 驗證錯誤回應

```json
{
  "code": 400,
  "message": "資料驗證失敗",
  "data": {
    "empCode": "員工編號不能為空",
    "empName": "員工姓名長度必須在2-50個字元之間"
  },
  "timestamp": "2025-01-15T10:00:00Z",
  "path": "/api/v1/employees"
}
```

## 日誌記錄策略

### 1. 業務異常 - WARN 等級

```java
log.warn("Business exception: {}", ex.getMessage());
```

### 2. 驗證異常 - WARN 等級

```java
log.warn("Validation failed: {}", ex.getMessage());
```

### 3. 系統異常 - ERROR 等級

```java
log.error("System exception: {}", ex.getMessage(), ex);
```

## 注意事項

1. 異常類別要繼承適當的父類別
2. 使用 `@ResponseStatus` 指定 HTTP 狀態碼
3. 提供有意義的錯誤訊息
4. 記錄適當等級的日誌
5. 不要在異常訊息中洩露敏感資訊
6. 使用統一的錯誤回應格式
7. 驗證異常要返回具體的欄位錯誤
8. 系統異常要記錄完整的堆疊追蹤
9. 每個異常類別都要有完整的 JavaDoc 註釋
10. 全域處理器要處理所有可能的異常類型

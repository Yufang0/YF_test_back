# Employee 欄位新增報告

## 概要

成功新增了兩個遺漏的欄位到員工管理系統中：

- `job_title` (varchar(100)) - 職位名稱
- `resign_date` (date) - 離職日期

## 欄位位置

按照需求，新欄位被放置在指定位置：

- `job_title` 放在 `dep_id` 的下一個位置
- `resign_date` 放在 `hire_date` 的下一個位置

## 修改的檔案

### 1. EmployeeEntity.java

**位置：** `src/main/java/com/example/demo/entity/EmployeeEntity.java`

**新增欄位：**

```java
// 職位（放在 deptId 之後）
@Column(name = "job_title", length = 100)
@Comment("職位")
private String jobTitle;

// 離職日期（放在 hireDate 之後）
@Column(name = "resign_date")
@Comment("離職日期")
private LocalDate resignDate;
```

### 2. EmployeeCreateDto.java

**位置：** `src/main/java/com/example/demo/dto/EmployeeCreateDto.java`

**新增欄位：**

```java
@JsonProperty("job_title")
@Size(max = 100, message = "職位名稱不能超過100個字元")
private String jobTitle;

@JsonProperty("resign_date")
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate resignDate;
```

### 3. EmployeeUpdateDto.java

**位置：** `src/main/java/com/example/demo/dto/EmployeeUpdateDto.java`

**新增欄位：**

```java
@JsonProperty("job_title")
@Size(max = 100, message = "職位名稱不能超過100個字元")
private String jobTitle;

@JsonProperty("resign_date")
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate resignDate;
```

### 4. EmployeeVo.java

**位置：** `src/main/java/com/example/demo/vo/EmployeeVo.java`

**新增欄位：**

```java
@JsonProperty("job_title")
private String jobTitle;

@JsonProperty("resign_date")
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate resignDate;
```

### 5. EmployeeService.java

**位置：** `src/main/java/com/example/demo/service/EmployeeService.java`

**修改內容：**

- 更新 `convertCreateDtoToEntity` 方法，加入新欄位對應
- 更新 `updateEntityFromDto` 方法，加入新欄位更新邏輯
- 更新 `convertEntityToVo` 方法，加入新欄位對應
- 新增 `searchEmployeesByJobTitle` 方法用於職位搜尋

**新增方法：**

```java
/**
 * 根據職位模糊搜尋員工
 *
 * @param jobTitle 職位名稱（支援部分匹配）
 * @return 員工列表
 */
public List<EmployeeVo> searchEmployeesByJobTitle(String jobTitle) {
    log.debug("Searching employees by job title: {}", jobTitle);

    List<EmployeeEntity> entities = employeeRepository.findByJobTitleContaining(jobTitle);
    return entities.stream()
            .map(this::convertEntityToVo)
            .collect(Collectors.toList());
}
```

### 6. EmployeeRepository.java

**位置：** `src/main/java/com/example/demo/repo/EmployeeRepository.java`

**新增方法：**

```java
/**
 * 根據職位模糊搜尋員工
 *
 * @param jobTitle 職位名稱
 * @return 員工列表
 */
List<EmployeeEntity> findByJobTitleContaining(String jobTitle);
```

### 7. EmployeeController.java

**位置：** `src/main/java/com/example/demo/controller/EmployeeController.java`

**新增 API 端點：**

```java
/**
 * 根據職位模糊搜尋員工
 *
 * @param jobTitle 職位名稱
 * @return 員工列表
 */
@GetMapping("/search/job-title")
@Operation(summary = "根據職位搜尋員工", description = "根據職位名稱模糊搜尋員工")
public ResponseEntity<ApiResponse<List<EmployeeVo>>> searchEmployeesByJobTitle(
    @Parameter(description = "職位名稱（支援部分匹配）", required = true) @RequestParam String jobTitle) {

    log.debug("Searching employees by job title: {}", jobTitle);

    List<EmployeeVo> employees = employeeService.searchEmployeesByJobTitle(jobTitle);
    ApiResponse<List<EmployeeVo>> response = ApiResponse.success(employees);

    return ResponseEntity.ok(response);
}
```

## 新增功能

1. **職位搜尋 API：** `GET /employees/search/job-title?jobTitle={職位名稱}`
2. **資料驗證：** job_title 最大長度 100 字元
3. **JSON 格式化：** resign_date 使用 "yyyy-MM-dd" 格式

## 編譯狀態

✅ **編譯成功** - 所有檔案均無錯誤，Maven 編譯通過

## API 文件更新

- Swagger UI 會自動包含新的職位搜尋端點
- 所有 DTO 和 VO 都已更新 OpenAPI 文件註解

## 資料庫欄位對應

| Java 欄位  | 資料庫欄位  | 類型    | 長度 | 說明     |
| ---------- | ----------- | ------- | ---- | -------- |
| jobTitle   | job_title   | VARCHAR | 100  | 職位名稱 |
| resignDate | resign_date | DATE    | -    | 離職日期 |

## 總結

所有新增欄位已成功整合到員工管理系統的各個層級：

- 實體層（Entity）
- 資料存取層（Repository）
- 業務邏輯層（Service）
- 控制器層（Controller）
- 資料傳輸物件（DTO/VO）

系統現在支援完整的職位和離職日期管理功能。

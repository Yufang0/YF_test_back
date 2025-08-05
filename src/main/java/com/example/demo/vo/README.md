# VO (View Object) 資料夾

## 概述

此資料夾包含所有 View Object (VO) 類別。VO 主要用於 API 回應，向前端返回格式化的資料。VO 與 DTO 的主要區別是 VO 專注於資料展示，通常包含計算欄位、格式化的日期時間和狀態文字等。

## 資料夾內容

- **EmployeeVo.java** - 員工視圖物件，用於 API 回應
- **ApiResponse.java** - 統一 API 回應格式
- **PageResponse.java** - 分頁回應格式

## 重要慣例

### 命名規範

- 所有 VO 類別以 `Vo` 結尾
- 類別名稱使用 PascalCase
- 統一回應格式使用 `ApiResponse`
- 分頁回應使用 `PageResponse`

### JSON 格式化

- 使用 `@JsonProperty` 指定 JSON 欄位名稱
- 使用 `@JsonFormat` 格式化日期時間
- 提供人性化的顯示欄位（如狀態文字）

### 資料展示優化

- 移除敏感欄位（密碼等）
- 增加計算欄位（如工作天數）
- 格式化日期時間為易讀格式
- 提供狀態的文字描述

## 快速開始

### 建立新的 VO

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExampleVo {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("status_text")
    private String statusText;

    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonProperty("working_days")
    private Long workingDays;
}
```

### 統一回應格式使用

```java
// 成功回應
ApiResponse<EmployeeVo> response = ApiResponse.success(employeeVo);

// 成功回應（自定義訊息）
ApiResponse<EmployeeVo> response = ApiResponse.success(employeeVo, "查詢成功");

// 錯誤回應
ApiResponse<Void> response = ApiResponse.error(404, "員工不存在");
```

## 最小工作範例

```java
/**
 * 員工視圖物件
 * 用於 API 回應，包含格式化的員工資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeVo {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("emp_code")
    private String empCode;

    @JsonProperty("emp_name")
    private String empName;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("status_text")
    private String statusText;

    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonProperty("working_days")
    private Long workingDays;
}
```

## VO 類型分類

### 1. 實體 VO

- 對應具體業務實體的視圖物件
- 包含實體的完整或部分資訊
- 例如：`EmployeeVo`, `DepartmentVo`

### 2. 統一回應 VO

- **ApiResponse<T>** - 標準 API 回應格式
- **PageResponse<T>** - 分頁回應格式
- **ErrorResponse** - 錯誤回應格式

### 3. 摘要/列表 VO

- 用於列表顯示的簡化版本
- 只包含關鍵資訊
- 例如：`EmployeeSummaryVo`

## 統一回應格式結構

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // 實際資料
  },
  "timestamp": "2025-01-15T10:00:00Z",
  "path": "/api/v1/employees"
}
```

## 分頁回應格式結構

```json
{
  "code": 200,
  "message": "查詢成功",
  "data": {
    "content": [...],
    "page": 0,
    "size": 20,
    "total_elements": 100,
    "total_pages": 5,
    "has_next": true,
    "has_previous": false
  }
}
```

## 常用 JSON 格式化

### 日期時間格式化

```java
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime createTime;

@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate birthDate;
```

### 數值格式化

```java
@JsonFormat(shape = JsonFormat.Shape.STRING)
private BigDecimal amount;
```

### 狀態文字轉換

```java
// 在 Service 層進行轉換
vo.setStatusText(entity.getIsActive() ? "啟用" : "停用");
```

## Entity 到 VO 轉換最佳實踐

```java
// 在 Service 中進行轉換
private EmployeeVo convertEntityToVo(EmployeeEntity entity) {
    EmployeeVo vo = new EmployeeVo();
    vo.setId(entity.getId());
    vo.setEmpCode(entity.getEmpCode());
    vo.setEmpName(entity.getEmpName());
    vo.setIsActive(entity.getIsActive());

    // 狀態文字轉換
    vo.setStatusText(entity.getIsActive() ? "啟用" : "停用");

    // 計算欄位
    if (entity.getHireDate() != null) {
        vo.setWorkingDays(ChronoUnit.DAYS.between(
            entity.getHireDate(), LocalDate.now()));
    }

    return vo;
}
```

## 注意事項

1. VO 只用於資料展示，不包含業務邏輯
2. 使用 `@JsonFormat` 提供友善的日期時間格式
3. 提供狀態的文字描述，方便前端顯示
4. 移除敏感資訊（如密碼欄位）
5. 可以包含計算欄位（如年齡、工作天數）
6. 所有 VO 都要有完整的 JavaDoc 註釋
7. 使用統一的回應格式保持 API 一致性
8. 分頁回應要包含完整的分頁資訊
9. 錯誤回應要提供清楚的錯誤訊息和錯誤碼

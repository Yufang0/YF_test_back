# DTO 資料夾

## 概述

此資料夾包含所有 Data Transfer Object (DTO) 類別。DTO 用於在不同層之間傳輸資料，特別是在 API 請求和回應中。DTO 幫助控制資料的輸入輸出格式，並提供資料驗證功能。

## 資料夾內容

- **EmployeeCreateDto.java** - 員工建立請求 DTO
- **EmployeeUpdateDto.java** - 員工更新請求 DTO
- **EmployeeSummaryDto.java** - 員工摘要 DTO

## 重要慣例

### 命名規範

- 所有 DTO 類別以 `Dto` 結尾
- 類別名稱使用 PascalCase
- 根據用途命名：`CreateDto`, `UpdateDto`, `QueryDto`, `SummaryDto` 等

### JSON 屬性對應

- 使用 `@JsonProperty` 明確指定 JSON 欄位名稱
- JSON 欄位名稱使用 snake_case 風格
- 確保前端和後端欄位名稱一致

### 資料驗證

- 使用 Jakarta Bean Validation 註解進行輸入驗證
- 常用驗證註解：`@NotNull`, `@NotBlank`, `@Size`, `@Email`, `@Pattern` 等
- 提供中文錯誤訊息

## 快速開始

### 建立新的 Create DTO

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExampleCreateDto {

    @JsonProperty("name")
    @NotBlank(message = "名稱不能為空")
    @Size(max = 50, message = "名稱長度不能超過50個字元")
    private String name;

    @JsonProperty("email")
    @Email(message = "請輸入有效的電子郵箱格式")
    private String email;

    @JsonProperty("is_active")
    @NotNull(message = "狀態不能為空")
    private Boolean isActive;
}
```

### 建立新的 Update DTO

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExampleUpdateDto {

    @JsonProperty("id")
    @NotNull(message = "ID 不能為空")
    @Positive(message = "ID 必須為正整數")
    private Integer id;

    @JsonProperty("name")
    @Size(max = 50, message = "名稱長度不能超過50個字元")
    private String name;

    // 更新 DTO 中的欄位通常是可選的（不使用 @NotNull）
}
```

## 最小工作範例

```java
/**
 * 員工建立請求 DTO
 * 用於接收建立員工的 API 請求資料
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateDto {

    @JsonProperty("emp_code")
    @NotBlank(message = "員工編號不能為空")
    @Size(max = 20, message = "員工編號長度不能超過20個字元")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "員工編號只能包含大寫字母和數字")
    private String empCode;

    @JsonProperty("emp_name")
    @NotBlank(message = "員工姓名不能為空")
    @Size(min = 2, max = 50, message = "員工姓名長度必須在2-50個字元之間")
    private String empName;

    @JsonProperty("dept_id")
    @NotNull(message = "部門 ID 不能為空")
    @Positive(message = "部門 ID 必須為正整數")
    private Integer deptId;
}
```

## DTO 類型分類

### 1. 請求 DTO (Request DTO)

- **CreateDto** - 用於建立操作，包含必要欄位驗證
- **UpdateDto** - 用於更新操作，多數欄位為可選
- **QueryDto** - 用於查詢參數，包含分頁和篩選條件

### 2. 回應 DTO (Response DTO)

- **SummaryDto** - 摘要資訊，包含關鍵欄位
- **DetailDto** - 詳細資訊，包含完整資料

### 3. 內部傳輸 DTO

- 用於不同服務間的資料傳輸
- 通常不需要驗證註解

## 常用驗證註解

### 基本驗證

- `@NotNull` - 不能為 null
- `@NotBlank` - 字串不能為空白
- `@NotEmpty` - 集合不能為空

### 字串驗證

- `@Size(min=2, max=50)` - 字串長度限制
- `@Pattern(regexp="...")` - 正規表示式驗證
- `@Email` - 電子郵箱格式驗證

### 數值驗證

- `@Positive` - 正數
- `@Min(1)` / `@Max(100)` - 數值範圍
- `@DecimalMin` / `@DecimalMax` - 小數範圍

### 日期驗證

- `@Past` - 過去日期
- `@Future` - 未來日期
- `@PastOrPresent` - 過去或現在日期

## 注意事項

1. DTO 類別使用 `@Data` 註解自動產生 getter/setter
2. 使用 `@JsonProperty` 明確指定 JSON 欄位名稱
3. 驗證訊息要使用中文，提供友善的錯誤提示
4. Create DTO 通常包含更嚴格的驗證
5. Update DTO 的欄位多為可選，允許部分更新
6. 避免在 DTO 中包含業務邏輯
7. 每個 DTO 都要有完整的 JavaDoc 註釋
8. 日期欄位使用 `@JsonFormat` 指定格式

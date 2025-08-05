# Employee Management System - tb_employee 完整實作

## 概述

根據專案指導原則，已成功為 `tb_employee` 資料表建立完整的 Spring Boot 後端實作，包含 JPA Entity、Repository、DTO、Service、Controller 等所有必要的類別，每個類別都包含完整的 JavaDoc 註釋。

## 已建立的檔案

### 1. Entity 層

- `EmployeeEntity.java` - 員工實體類別，對應 tb_employee 資料表

### 2. Repository 層

- `EmployeeRepository.java` - 員工資料存取介面

### 3. DTO 層

- `EmployeeCreateDto.java` - 員工建立請求 DTO
- `EmployeeUpdateDto.java` - 員工更新請求 DTO
- `EmployeeSummaryDto.java` - 員工摘要 DTO

### 4. VO 層

- `EmployeeVo.java` - 員工視圖物件
- `ApiResponse.java` - 統一 API 回應格式
- `PageResponse.java` - 分頁回應格式

### 5. Service 層

- `EmployeeService.java` - 員工業務邏輯服務

### 6. Controller 層

- `EmployeeController.java` - 員工控制器

### 7. Exception 層

- `EmployeeNotFoundException.java` - 員工未找到異常
- `EmployeeAlreadyExistsException.java` - 員工已存在異常
- `GlobalExceptionHandler.java` - 全域異常處理器

### 8. 文件

- 每個主要資料夾都包含詳細的 `README.md` 文件

## 資料表對應

### tb_employee 表結構

```sql
CREATE TABLE tb_employee (
    id INT PRIMARY KEY AUTO_INCREMENT,
    emp_code VARCHAR(20) UNIQUE,
    emp_name VARCHAR(50) NOT NULL,
    emp_email VARCHAR(50),
    dept_id INT,
    hire_date DATE,
    is_active TINYINT(1),
    create_time DATETIME,
    create_user VARCHAR(20),
    FOREIGN KEY (dept_id) REFERENCES tb_department(id)
);
```

## API 端點

### 員工管理 API (`/api/v1/employees`)

| HTTP 方法 | 端點                           | 描述                     |
| --------- | ------------------------------ | ------------------------ |
| POST      | `/`                            | 建立新員工               |
| GET       | `/{id}`                        | 根據 ID 查詢員工         |
| GET       | `/code/{empCode}`              | 根據員工編號查詢員工     |
| GET       | `/`                            | 分頁查詢所有員工         |
| GET       | `/status/{isActive}`           | 根據狀態分頁查詢員工     |
| GET       | `/department/{deptId}`         | 查詢部門員工列表         |
| GET       | `/search?empName=`             | 根據姓名模糊搜尋員工     |
| GET       | `/hire-date-range`             | 根據入職日期範圍查詢員工 |
| PUT       | `/{id}`                        | 更新員工資訊             |
| DELETE    | `/{id}`                        | 刪除員工                 |
| GET       | `/department/{deptId}/summary` | 取得部門員工摘要         |
| GET       | `/department/{deptId}/count`   | 統計部門員工數           |

## 功能特色

### 1. 完整的 CRUD 操作

- 建立、查詢、更新、刪除員工資料
- 支援多種查詢條件（ID、編號、部門、狀態、姓名、日期範圍）

### 2. 資料驗證

- 使用 Jakarta Bean Validation 進行輸入驗證
- 自定義驗證規則和中文錯誤訊息
- 業務邏輯驗證（如員工編號唯一性）

### 3. 分頁查詢

- 支援分頁、排序功能
- 統一的分頁回應格式

### 4. 異常處理

- 全域異常處理器
- 統一的錯誤回應格式
- 適當的 HTTP 狀態碼

### 5. 日誌記錄

- 使用 SLF4J 進行日誌記錄
- 關鍵操作的日誌追蹤

### 6. API 文件

- 完整的 Swagger/OpenAPI 註解
- 詳細的參數說明和回應範例

## 技術實作細節

### 1. 遵循專案指導原則

- 使用 Spring Boot 3.5.4 + Java 21
- 採用 Jakarta EE 10 註解
- 使用 Lombok 簡化程式碼
- 建構函式依賴注入

### 2. 設計模式

- 分層架構：Controller → Service → Repository → Entity
- DTO/VO 模式進行資料傳輸
- 統一回應格式

### 3. 資料轉換

- Entity ↔ DTO 轉換
- Entity → VO 轉換
- 包含計算欄位（如工作天數）

### 4. 交易管理

- 讀取操作使用 `@Transactional(readOnly = true)`
- 寫入操作使用 `@Transactional`

## 程式碼品質保證

### 1. 註釋和文件

- 每個類別和方法都有完整的 JavaDoc
- 詳細的程式碼註釋說明業務邏輯

### 2. 命名規範

- 遵循 Java 命名慣例
- 清楚表達類別和方法的用途

### 3. 錯誤處理

- 完整的異常處理機制
- 有意義的錯誤訊息

### 4. 日誌記錄

- 適當的日誌等級
- 關鍵操作的追蹤記錄

## 使用方式

### 1. 編譯專案

```bash
mvn clean compile
```

### 2. 執行應用程式

```bash
mvn spring-boot:run
```

### 3. 存取 API 文件

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

### 4. 測試 API

使用 Postman 或其他 REST 客戶端測試各個端點

## 範例請求

### 建立員工

```json
POST /api/v1/employees
{
  "emp_code": "EMP001",
  "emp_name": "張三",
  "emp_email": "zhang.san@example.com",
  "dept_id": 1,
  "hire_date": "2024-01-15",
  "is_active": true,
  "create_user": "admin"
}
```

### 查詢員工

```json
GET /api/v1/employees/1

回應:
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "emp_code": "EMP001",
    "emp_name": "張三",
    "emp_email": "zhang.san@example.com",
    "dept_id": 1,
    "is_active": true,
    "status_text": "啟用",
    "hire_date": "2024-01-15",
    "create_time": "2024-01-15 10:00:00",
    "create_user": "admin",
    "working_days": 365
  },
  "timestamp": "2025-01-15T10:00:00Z"
}
```

## 後續擴充建議

1. 新增員工與部門的關聯查詢
2. 實作員工權限管理
3. 新增員工匯入/匯出功能
4. 新增員工異動歷史記錄
5. 實作更複雜的查詢條件
6. 新增快取機制提升效能

此實作完全遵循專案的程式碼規範和最佳實踐，提供了完整、健壯、可維護的員工管理功能。

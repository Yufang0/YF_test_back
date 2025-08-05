# Repository 資料夾

## 概述

此資料夾包含所有資料存取層 (Data Access Layer) 的 Repository 介面。Repository 負責與資料庫進行互動，提供資料的 CRUD 操作以及自定義查詢方法。

## 資料夾內容

- **EmployeeRepository.java** - 員工資料存取介面，提供員工相關的資料庫操作

## 重要慣例

### 命名規範

- 所有 Repository 介面以 `Repository` 結尾
- 介面名稱使用 PascalCase
- 查詢方法遵循 Spring Data JPA 命名慣例

### 繼承結構

- 繼承 `JpaRepository<Entity, ID>` 獲得基本 CRUD 操作
- 泛型參數：第一個是實體類別，第二個是主鍵類型

### 方法命名慣例

- `findBy...` - 查詢方法
- `countBy...` - 統計方法
- `existsBy...` - 存在性檢查
- `deleteBy...` - 刪除方法

## 快速開始

### 建立新的 Repository

```java
@Repository
public interface ExampleRepository extends JpaRepository<ExampleEntity, Integer> {

    // 根據欄位查詢
    Optional<ExampleEntity> findByName(String name);

    // 模糊查詢
    List<ExampleEntity> findByNameContaining(String name);

    // 分頁查詢
    Page<ExampleEntity> findByStatus(String status, Pageable pageable);

    // 統計查詢
    long countByStatus(String status);

    // 存在性檢查
    boolean existsByName(String name);
}
```

### 自定義查詢

```java
@Query("SELECT e FROM ExampleEntity e WHERE e.status = :status AND e.createTime > :date")
List<ExampleEntity> findByStatusAndCreateTimeAfter(@Param("status") String status, @Param("date") LocalDateTime date);

@Query("SELECT new com.example.dto.SummaryDto(e.id, e.name) FROM ExampleEntity e WHERE e.status = 'ACTIVE'")
List<SummaryDto> findActiveSummary();
```

## 最小工作範例

```java
/**
 * 員工資料存取介面
 * 提供員工相關的資料庫操作方法
 */
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    /**
     * 根據員工編號查詢員工
     */
    Optional<EmployeeEntity> findByEmpCode(String empCode);

    /**
     * 根據部門 ID 查詢員工列表
     */
    List<EmployeeEntity> findByDeptId(Integer deptId);

    /**
     * 根據員工狀態分頁查詢
     */
    Page<EmployeeEntity> findByIsActive(Boolean isActive, Pageable pageable);

    /**
     * 檢查員工編號是否存在
     */
    boolean existsByEmpCode(String empCode);
}
```

## 查詢方法類型

### 1. 基本查詢方法

- `findByProperty` - 根據屬性查詢
- `findByPropertyAndProperty2` - 多條件查詢
- `findByPropertyContaining` - 模糊查詢
- `findByPropertyBetween` - 範圍查詢

### 2. 分頁和排序

```java
Page<Entity> findByProperty(String value, Pageable pageable);
List<Entity> findByPropertyOrderByCreateTimeDesc(String value);
```

### 3. 統計方法

```java
long countByProperty(String value);
boolean existsByProperty(String value);
```

### 4. 自定義 JPQL 查詢

```java
@Query("SELECT e FROM Entity e WHERE e.property = ?1")
List<Entity> customQuery(String value);
```

## 注意事項

1. 所有 Repository 介面都要加上 `@Repository` 註解
2. 方法名稱要清楚表達查詢意圖
3. 複雜查詢建議使用 `@Query` 註解
4. 分頁查詢要使用 `Pageable` 參數
5. 所有方法都要有完整的 JavaDoc 註釋
6. 使用 `Optional<T>` 包裝可能為空的單一結果
7. 避免在 Repository 中寫業務邏輯

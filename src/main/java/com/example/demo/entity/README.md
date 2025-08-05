# Entity 資料夾

## 概述

此資料夾包含所有 JPA Entity 類別，這些類別直接對應資料庫表結構。Entity 類別是應用程式的資料模型核心，負責定義資料的結構和關聯性。

## 資料夾內容

- **EmployeeEntity.java** - 員工實體類別，對應 `tb_employee` 資料表

## 重要慣例

### 命名規範

- 所有 Entity 類別以 `Entity` 結尾
- 類別名稱使用 PascalCase
- 資料表名稱使用 snake_case，通過 `@Table(name = "table_name")` 指定
- 欄位名稱透過 `@Column(name = "column_name")` 明確對應

### 註解使用

- `@Entity` - 標記為 JPA 實體
- `@Table(name = "表名")` - 指定對應的資料表名稱
- `@Id` - 標記主鍵欄位
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` - 自動遞增主鍵
- `@Column` - 明確指定欄位對應和約束
- `@CreationTimestamp` - 自動設定建立時間
- `@UpdateTimestamp` - 自動設定更新時間

### Lombok 註解

- `@Data` - 自動產生 getter/setter, toString, equals, hashCode
- `@NoArgsConstructor` - 產生無參建構函式
- `@AllArgsConstructor` - 產生全參建構函式

## 快速開始

### 建立新的 Entity

```java
@Entity
@Table(name = "tb_example")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExampleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "create_time", nullable = false)
    @CreationTimestamp
    private LocalDateTime createTime;
}
```

## 最小工作範例

```java
/**
 * 員工實體類別對應 tb_employee 表
 * 包含員工基本資訊如姓名、編號、部門等
 */
@Entity
@Table(name = "tb_employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "emp_code", unique = true, length = 20)
    private String empCode;

    @Column(name = "emp_name", nullable = false, length = 50)
    private String empName;

    // 其他欄位...
}
```

## 注意事項

1. 所有 Entity 都應該實作 `Serializable` 介面
2. 必須明確指定資料表名稱和欄位名稱
3. 主鍵欄位統一命名為 `id`
4. 時間欄位使用 `LocalDateTime` 或 `LocalDate`
5. 布林欄位在資料庫中對應 `TINYINT(1)`
6. 所有 Entity 類別都必須包含完整的 JavaDoc 註釋

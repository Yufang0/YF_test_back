# Service 資料夾

## 概述

此資料夾包含所有業務邏輯層 (Business Logic Layer) 的 Service 類別。Service 負責處理業務規則、協調不同的 Repository、進行資料轉換，以及管理交易。Service 是連接 Controller 和 Repository 之間的橋樑。

## 資料夾內容

- **EmployeeService.java** - 員工業務邏輯服務，處理員工相關的業務操作

## 重要慣例

### 命名規範

- 所有 Service 類別以 `Service` 結尾
- 類別名稱使用 PascalCase
- 方法名稱使用動詞開頭，清楚表達業務操作

### 註解使用

- `@Service` - 標記為 Spring 服務元件
- `@Transactional` - 交易管理
- `@RequiredArgsConstructor` - Lombok 建構函式注入
- `@Slf4j` - 日誌記錄

### 交易管理

- 讀取操作使用 `@Transactional(readOnly = true)`
- 寫入操作使用 `@Transactional`
- 重要業務操作要有適當的交易邊界

## 快速開始

### 建立新的 Service

```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ExampleService {

    private final ExampleRepository exampleRepository;

    /**
     * 建立新記錄
     */
    @Transactional
    public ExampleVo createExample(ExampleCreateDto createDto) {
        log.info("Creating example: name={}", createDto.getName());

        ExampleEntity entity = convertCreateDtoToEntity(createDto);
        ExampleEntity savedEntity = exampleRepository.save(entity);

        log.info("Example created successfully: id={}", savedEntity.getId());
        return convertEntityToVo(savedEntity);
    }

    /**
     * 根據 ID 查詢記錄
     */
    public ExampleVo getExampleById(Integer id) {
        ExampleEntity entity = exampleRepository.findById(id)
            .orElseThrow(() -> new ExampleNotFoundException("記錄不存在: " + id));
        return convertEntityToVo(entity);
    }
}
```

## 最小工作範例

```java
/**
 * 員工業務邏輯服務
 * 處理員工相關的所有業務操作
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * 建立新員工
     */
    @Transactional
    public EmployeeVo createEmployee(EmployeeCreateDto createDto) {
        log.info("Creating employee: empCode={}", createDto.getEmpCode());

        // 業務規則驗證
        if (employeeRepository.existsByEmpCode(createDto.getEmpCode())) {
            throw new EmployeeAlreadyExistsException("員工編號已存在: " + createDto.getEmpCode());
        }

        // 資料轉換和保存
        EmployeeEntity entity = convertCreateDtoToEntity(createDto);
        EmployeeEntity savedEntity = employeeRepository.save(entity);

        log.info("Employee created successfully: id={}", savedEntity.getId());
        return convertEntityToVo(savedEntity);
    }

    /**
     * 根據 ID 查詢員工
     */
    public EmployeeVo getEmployeeById(Integer id) {
        EmployeeEntity entity = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException("員工不存在: " + id));
        return convertEntityToVo(entity);
    }
}
```

## Service 層職責

### 1. 業務規則實現

- 驗證業務邏輯
- 執行業務流程
- 確保資料一致性

### 2. 資料轉換

- DTO 到 Entity 的轉換
- Entity 到 VO 的轉換
- 資料格式化和計算

### 3. 交易管理

- 定義交易邊界
- 處理交易回滾
- 確保資料完整性

### 4. 異常處理

- 拋出業務異常
- 記錄錯誤日誌
- 提供有意義的錯誤訊息

## 常用設計模式

### 1. 建構函式注入

```java
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
}
```

### 2. 轉換方法模式

```java
private EmployeeEntity convertCreateDtoToEntity(EmployeeCreateDto dto) {
    EmployeeEntity entity = new EmployeeEntity();
    entity.setEmpCode(dto.getEmpCode());
    entity.setEmpName(dto.getEmpName());
    return entity;
}

private EmployeeVo convertEntityToVo(EmployeeEntity entity) {
    EmployeeVo vo = new EmployeeVo();
    vo.setId(entity.getId());
    vo.setEmpCode(entity.getEmpCode());
    return vo;
}
```

### 3. 業務驗證模式

```java
@Transactional
public EmployeeVo updateEmployee(EmployeeUpdateDto updateDto) {
    // 1. 存在性檢查
    EmployeeEntity existingEntity = employeeRepository.findById(updateDto.getId())
        .orElseThrow(() -> new EmployeeNotFoundException("員工不存在"));

    // 2. 業務規則驗證
    validateBusinessRules(updateDto, existingEntity);

    // 3. 執行更新
    updateEntityFromDto(existingEntity, updateDto);
    EmployeeEntity savedEntity = employeeRepository.save(existingEntity);

    // 4. 返回結果
    return convertEntityToVo(savedEntity);
}
```

## 日誌記錄最佳實踐

### 1. 關鍵操作記錄

```java
@Transactional
public EmployeeVo createEmployee(EmployeeCreateDto createDto) {
    log.info("Starting employee creation: empCode={}", createDto.getEmpCode());

    try {
        // 業務邏輯
        EmployeeEntity savedEntity = employeeRepository.save(entity);

        log.info("Employee created successfully: id={}, empCode={}",
            savedEntity.getId(), savedEntity.getEmpCode());

        return convertEntityToVo(savedEntity);
    } catch (Exception e) {
        log.error("Employee creation failed: empCode={}, error={}",
            createDto.getEmpCode(), e.getMessage(), e);
        throw e;
    }
}
```

### 2. 查詢操作記錄

```java
public EmployeeVo getEmployeeById(Integer id) {
    log.debug("Fetching employee by id: {}", id);
    // 查詢邏輯
}
```

## 異常處理策略

### 1. 業務異常

```java
if (employeeRepository.existsByEmpCode(empCode)) {
    throw new EmployeeAlreadyExistsException("員工編號已存在: " + empCode);
}
```

### 2. 資料不存在異常

```java
EmployeeEntity entity = employeeRepository.findById(id)
    .orElseThrow(() -> new EmployeeNotFoundException("員工不存在: " + id));
```

### 3. 系統異常包装

```java
try {
    // 業務操作
} catch (Exception e) {
    log.error("Unexpected error occurred: {}", e.getMessage(), e);
    throw new RuntimeException("系統內部錯誤: " + e.getMessage(), e);
}
```

## 注意事項

1. Service 類別要加上 `@Service` 註解
2. 使用建構函式注入，避免欄位注入
3. 讀取操作使用 `@Transactional(readOnly = true)`
4. 寫入操作使用 `@Transactional`
5. 重要操作要記錄日誌
6. 拋出有意義的業務異常
7. 進行適當的資料驗證
8. 將 Entity 和 DTO/VO 轉換邏輯封裝在私有方法中
9. 每個方法都要有完整的 JavaDoc 註釋
10. 避免在 Service 中直接操作 HttpServletRequest 等 Web 層物件

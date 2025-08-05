# MySQL 遷移指南

## 已完成的遷移步驟

### 1. POM.XML 更新

- ✅ 將 H2 dependency 範圍改為 `test`，僅用於測試環境
- ✅ 保留 MySQL connector dependency 用於生產環境

### 2. Application.properties 配置

- ✅ 新增 MySQL 資料庫連接配置
- ✅ 設定 JPA/Hibernate MySQL 方言
- ✅ 配置連接池優化參數
- ✅ 啟用效能優化設定

### 3. 實體類別優化

- ✅ 更新 EmployeeEntity 支援 MySQL
- ✅ 新增資料庫索引定義
- ✅ 修正 Boolean 欄位為 MySQL TINYINT(1)
- ✅ 增加 email 欄位長度
- ✅ 新增 UpdateTimestamp 支援

### 4. 測試環境配置

- ✅ 建立獨立的測試配置文件
- ✅ 測試環境繼續使用 H2 記憶體資料庫

### 5. 資料庫配置類別

- ✅ 建立 DatabaseConfig 配置類別
- ✅ 啟用 JPA Repositories 和事務管理

## 使用說明

### 1. 資料庫準備

執行 `src/main/resources/mysql-init.sql` 來建立資料庫和使用者：

```sql
mysql -u root -p < src/main/resources/mysql-init.sql
```

### 2. 應用程式啟動

確保 MySQL 服務正在運行，然後啟動 Spring Boot 應用程式：

```bash
mvn spring-boot:run
```

### 3. 表格自動建立

由於設定了 `spring.jpa.hibernate.ddl-auto=update`，Hibernate 會自動建立或更新表格結構。

### 4. 測試執行

執行單元測試時會自動使用 H2 記憶體資料庫：

```bash
mvn test
```

## 資料庫連接資訊

- **Host**: localhost
- **Port**: 3306
- **Database**: yf_crouse
- **Username**: c_user
- **Password**: user0818

## 注意事項

1. **首次啟動**: 確保 MySQL 服務正在運行且資料庫已建立
2. **字元編碼**: 資料庫使用 UTF-8 編碼支援中文字符
3. **時區設定**: 設定為 Asia/Taipei 時區
4. **SSL**: 已停用 SSL 以簡化本地開發
5. **效能**: 已優化連接池和批次處理設定

## 常見問題

### 連接失敗

- 檢查 MySQL 服務是否啟動
- 確認資料庫和使用者是否正確建立
- 驗證防火牆設定

### 字符編碼問題

- 確保資料庫使用 utf8mb4 字符集
- 檢查連接 URL 中的編碼參數

### 效能問題

- 監控連接池使用情況
- 調整批次處理大小
- 檢查索引是否正確建立

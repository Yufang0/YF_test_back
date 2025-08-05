# MySQL 連接問題故障排除指南

## 🔧 已修正的問題

### 1. Hibernate 方言警告

- **問題**: `MySQLDialect does not need to be specified explicitly`
- **解決**: 移除 `spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect`
- **狀態**: ✅ 已修正

### 2. 連接池資訊顯示 "undefined/unknown"

- **問題**: HikariCP 連接池資訊顯示不正確
- **解決**:
  - 添加 `spring.datasource.hikari.pool-name=YF-HikariCP`
  - 添加 `spring.datasource.hikari.auto-commit=true`
  - 新增連接監控組件
- **狀態**: ✅ 已修正

## 🚀 新增功能

1. **DatabaseConnectionMonitor**: 應用程式啟動時自動檢查連接狀態
2. **DatabaseDiagnosticUtil**: 完整的資料庫診斷工具
3. **健康檢查端點增強**: 新增診斷端點 `/api/health/diagnostic`
4. **Spring Boot Actuator**: 添加監控和指標端點

## 📋 測試步驟

### 1. 啟動應用程式

```bash
mvn spring-boot:run
```

### 2. 檢查啟動日誌

應該看到類似以下的日誌：

```
=== 資料庫連接資訊 ===
資料庫產品名稱: MySQL
資料庫版本: 8.0.x
驅動程式名稱: MySQL Connector/J
...
=== HikariCP 連接池資訊 ===
連接池名稱: YF-HikariCP
最大連接數: 20
...
=== 資料庫連接測試成功 ===
```

### 3. 測試健康檢查端點

```bash
# 基本健康檢查
curl http://localhost:8080/api/health

# 資料庫健康檢查
curl http://localhost:8080/api/health/database

# 完整診斷
curl http://localhost:8080/api/health/diagnostic

# Spring Boot Actuator 健康檢查
curl http://localhost:8080/actuator/health
```

### 4. 檢查連接池狀態

```bash
curl http://localhost:8080/actuator/metrics/hikaricp.connections.active
curl http://localhost:8080/actuator/metrics/hikaricp.connections.idle
```

## 🐛 常見問題

### 問題 1: 連接被拒絕

```
java.sql.SQLException: Connection refused
```

**解決方案**:

1. 確認 MySQL 服務正在運行
2. 檢查 3306 端口是否開放
3. 驗證防火牆設定

### 問題 2: 認證失敗

```
java.sql.SQLException: Access denied for user 'c_user'@'localhost'
```

**解決方案**:

1. 執行資料庫初始化腳本: `mysql -u root -p < src/main/resources/mysql-init.sql`
2. 確認使用者權限: `SHOW GRANTS FOR 'c_user'@'localhost';`

### 問題 3: 資料庫不存在

```
java.sql.SQLException: Unknown database 'yf_crouse'
```

**解決方案**:

1. 建立資料庫: `CREATE DATABASE yf_crouse;`
2. 執行初始化腳本

### 問題 4: 時區問題

```
java.sql.SQLException: The server time zone value 'CST' is unrecognized
```

**解決方案**:

- 已在連接 URL 中設定 `serverTimezone=Asia/Taipei`

## 📊 監控和維護

### 連接池監控

- 訪問: `http://localhost:8080/actuator/metrics`
- 關鍵指標:
  - `hikaricp.connections.active`
  - `hikaricp.connections.idle`
  - `hikaricp.connections.total`

### 資料庫效能監控

- SQL 執行時間
- 連接獲取時間
- 錯誤率

### 日誌監控

- 連接洩漏警告
- 長時間執行的查詢
- 連接池耗盡警告

## 🔄 下一步建議

1. **生產環境配置**:

   - 設定 `spring.jpa.hibernate.ddl-auto=validate`
   - 調整連接池大小
   - 啟用 SSL 連接

2. **效能優化**:

   - 添加資料庫索引
   - 配置查詢緩存
   - 監控慢查詢

3. **安全性**:
   - 使用環境變數存儲敏感資訊
   - 限制資料庫使用者權限
   - 啟用 SSL 連接

package com.example.demo.util;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 * 資料庫診斷工具
 * <p>
 * 提供資料庫連接診斷和問題排查功能，
 * 幫助識別和解決連接相關問題。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-08-04
 */
@Component
@Slf4j
public class DatabaseDiagnosticUtil {

    @Autowired
    private DataSource dataSource;

    /**
     * 執行完整的資料庫診斷
     */
    public void runFullDiagnostic() {
        log.info("開始執行資料庫診斷...");

        testBasicConnection();
        checkConnectionPoolStatus();
        testDatabaseQueries();
        checkDatabaseTables();

        log.info("資料庫診斷完成");
    }

    /**
     * 測試基本連接
     */
    private void testBasicConnection() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            log.info("✅ 基本連接測試成功");
            log.info("   - 資料庫: {} {}",
                    metaData.getDatabaseProductName(),
                    metaData.getDatabaseProductVersion());
            log.info("   - JDBC Driver: {} {}",
                    metaData.getDriverName(),
                    metaData.getDriverVersion());
            log.info("   - 連接 URL: {}", metaData.getURL());

        } catch (Exception e) {
            log.error("❌ 基本連接測試失敗: {}", e.getMessage());
        }
    }

    /**
     * 檢查連接池狀態
     */
    private void checkConnectionPoolStatus() {
        try {
            if (dataSource instanceof HikariDataSource) {
                HikariDataSource hikariDataSource = (HikariDataSource) dataSource;

                log.info("✅ HikariCP 連接池狀態:");
                log.info("   - 連接池名稱: {}", hikariDataSource.getPoolName());
                log.info("   - 活躍連接數: {}", hikariDataSource.getHikariPoolMXBean().getActiveConnections());
                log.info("   - 空閒連接數: {}", hikariDataSource.getHikariPoolMXBean().getIdleConnections());
                log.info("   - 等待連接數: {}", hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
                log.info("   - 總連接數: {}", hikariDataSource.getHikariPoolMXBean().getTotalConnections());
                log.info("   - 最大連接數: {}", hikariDataSource.getMaximumPoolSize());

            } else {
                log.warn("⚠️  非 HikariCP 連接池，無法取得詳細狀態");
            }
        } catch (Exception e) {
            log.error("❌ 連接池狀態檢查失敗: {}", e.getMessage());
        }
    }

    /**
     * 測試資料庫查詢
     */
    private void testDatabaseQueries() {
        try (Connection connection = dataSource.getConnection()) {
            // 測試基本查詢
            try (var statement = connection.createStatement();
                    var resultSet = statement.executeQuery("SELECT 1 as test_value")) {

                if (resultSet.next()) {
                    int value = resultSet.getInt("test_value");
                    log.info("✅ 基本查詢測試成功: {}", value);
                }
            }

            // 測試時間查詢
            try (var statement = connection.createStatement();
                    var resultSet = statement.executeQuery("SELECT NOW() as current_time")) {

                if (resultSet.next()) {
                    String currentTime = resultSet.getString("current_time");
                    log.info("✅ 時間查詢測試成功: {}", currentTime);
                }
            }

        } catch (Exception e) {
            log.error("❌ 資料庫查詢測試失敗: {}", e.getMessage());
        }
    }

    /**
     * 檢查資料庫表格
     */
    private void checkDatabaseTables() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            try (ResultSet tables = metaData.getTables(null, null, "%", new String[] { "TABLE" })) {
                log.info("✅ 資料庫表格列表:");

                int tableCount = 0;
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    String tableType = tables.getString("TABLE_TYPE");
                    log.info("   - {} ({})", tableName, tableType);
                    tableCount++;
                }

                if (tableCount == 0) {
                    log.warn("⚠️  未找到任何表格，可能是首次啟動");
                } else {
                    log.info("✅ 總共找到 {} 個表格", tableCount);
                }
            }

        } catch (Exception e) {
            log.error("❌ 資料庫表格檢查失敗: {}", e.getMessage());
        }
    }
}

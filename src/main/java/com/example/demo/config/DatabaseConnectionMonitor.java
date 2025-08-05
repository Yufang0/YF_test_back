package com.example.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 * 資料庫連接監控組件
 * <p>
 * 在應用程式啟動完成後檢查並記錄資料庫連接詳細資訊，
 * 包含連接池狀態、資料庫版本等資訊。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-08-04
 */
@Component
@Slf4j
public class DatabaseConnectionMonitor {

    @Autowired
    private DataSource dataSource;

    /**
     * 應用程式啟動完成後檢查資料庫連接
     */
    @EventListener(ApplicationReadyEvent.class)
    public void checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            log.info("=== 資料庫連接資訊 ===");
            log.info("資料庫產品名稱: {}", metaData.getDatabaseProductName());
            log.info("資料庫版本: {}", metaData.getDatabaseProductVersion());
            log.info("驅動程式名稱: {}", metaData.getDriverName());
            log.info("驅動程式版本: {}", metaData.getDriverVersion());
            log.info("連接 URL: {}", metaData.getURL());
            log.info("使用者名稱: {}", metaData.getUserName());
            log.info("是否支援事務: {}", metaData.supportsTransactions());
            log.info("自動提交模式: {}", connection.getAutoCommit());
            log.info("事務隔離級別: {}", connection.getTransactionIsolation());

            // 如果是 HikariCP，顯示連接池資訊
            if (dataSource instanceof HikariDataSource) {
                HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
                log.info("=== HikariCP 連接池資訊 ===");
                log.info("連接池名稱: {}", hikariDataSource.getPoolName());
                log.info("最大連接數: {}", hikariDataSource.getMaximumPoolSize());
                log.info("最小空閒連接數: {}", hikariDataSource.getMinimumIdle());
                log.info("連接超時時間: {} ms", hikariDataSource.getConnectionTimeout());
                log.info("空閒超時時間: {} ms", hikariDataSource.getIdleTimeout());
                log.info("最大生命週期: {} ms", hikariDataSource.getMaxLifetime());
                log.info("洩漏檢測閾值: {} ms", hikariDataSource.getLeakDetectionThreshold());
            }

            log.info("=== 資料庫連接測試成功 ===");

        } catch (Exception e) {
            log.error("資料庫連接測試失敗: {}", e.getMessage(), e);
        }
    }
}

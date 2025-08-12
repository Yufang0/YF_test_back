package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 系統健康檢查控制器
 * <p>
 * 提供系統健康狀態檢查端點，包含資料庫連接狀態驗證。
 * 用於監控 MySQL 資料庫連接是否正常。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-08-04
 */
@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    @Autowired
    private DataSource dataSource;

    /**
     * 檢查資料庫連接狀態
     * 
     * @return 資料庫連接狀態資訊
     */
    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> checkDatabaseHealth() {
        Map<String, Object> healthStatus = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) {
                healthStatus.put("status", "UP");
                healthStatus.put("database", "MySQL");
                healthStatus.put("url", connection.getMetaData().getURL());
                healthStatus.put("driver", connection.getMetaData().getDriverName());
                healthStatus.put("version", connection.getMetaData().getDatabaseProductVersion());
                healthStatus.put("message", "Database connection is healthy");
                return ResponseEntity.ok(healthStatus);
            } else {
                healthStatus.put("status", "DOWN");
                healthStatus.put("message", "Database connection is not valid");
                return ResponseEntity.status(503).body(healthStatus);
            }
        } catch (Exception e) {
            healthStatus.put("status", "DOWN");
            healthStatus.put("error", e.getMessage());
            healthStatus.put("message", "Failed to connect to database");
            return ResponseEntity.status(503).body(healthStatus);
        }
    }

    /**
     * 系統整體健康檢查
     * 
     * @return 系統健康狀態
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> checkSystemHealth() {
        Map<String, Object> systemHealth = new HashMap<>();
        systemHealth.put("status", "UP");
        systemHealth.put("application", "YF_test_back");
        systemHealth.put("timestamp", java.time.LocalDateTime.now());
        systemHealth.put("message", "System is running normally");

        return ResponseEntity.ok(systemHealth);
    }
}

package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 資料庫連接測試 - 使用測試配置（H2）
 */
@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testDatabaseConnection() throws Exception {
        System.out.println("=== 測試資料庫連接 ===");

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            System.out.println("資料庫產品名稱: " + metaData.getDatabaseProductName());
            System.out.println("資料庫版本: " + metaData.getDatabaseProductVersion());
            System.out.println("驅動程式名稱: " + metaData.getDriverName());
            System.out.println("驅動程式版本: " + metaData.getDriverVersion());
            System.out.println("連接 URL: " + metaData.getURL());
            System.out.println("使用者名稱: " + metaData.getUserName());
            System.out.println("自動提交模式: " + connection.getAutoCommit());
            System.out.println("事務隔離級別: " + connection.getTransactionIsolation());
            System.out.println("=== 連接測試成功 ===");

        } catch (Exception e) {
            System.err.println("資料庫連接測試失敗: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

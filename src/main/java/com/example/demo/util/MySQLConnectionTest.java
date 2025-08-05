package com.example.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * MySQL 連接測試工具
 */
public class MySQLConnectionTest {

    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/yf_crouse?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Taipei";
    private static final String USERNAME = "c_user";
    private static final String PASSWORD = "user0818";

    public static void main(String[] args) {
        System.out.println("=== MySQL 連接測試 ===");

        try {
            // 加載驅動程式
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ MySQL 驅動程式載入成功");

            // 測試連接
            try (Connection connection = DriverManager.getConnection(MYSQL_URL, USERNAME, PASSWORD)) {
                System.out.println("✓ MySQL 連接成功");
                System.out.println("資料庫產品: " + connection.getMetaData().getDatabaseProductName());
                System.out.println("資料庫版本: " + connection.getMetaData().getDatabaseProductVersion());
                System.out.println("自動提交模式: " + connection.getAutoCommit());

                // 測試基本 SQL
                var stmt = connection.createStatement();
                var rs = stmt.executeQuery("SELECT 1 as test");
                if (rs.next()) {
                    System.out.println("✓ SQL 執行測試成功: " + rs.getInt("test"));
                }

                // 測試交易
                connection.setAutoCommit(false);
                System.out.println("✓ 交易模式設定成功");
                connection.rollback();
                System.out.println("✓ 交易回滾測試成功");

            } catch (SQLException e) {
                System.err.println("✗ 連接或 SQL 錯誤: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            System.err.println("✗ 驅動程式未找到: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ 未知錯誤: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== 測試完成 ===");
    }
}

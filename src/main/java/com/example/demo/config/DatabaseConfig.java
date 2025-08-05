package com.example.demo.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 資料庫配置類別
 * <p>
 * 配置 JPA 和 MySQL 相關設定，包含實體掃描、Repository 掃描和事務管理。
 * 專為 MySQL 資料庫優化設計。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-08-04
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.example.demo.repo")
@EntityScan(basePackages = "com.example.demo.entity")
@EnableTransactionManagement
public class DatabaseConfig {

    // MySQL 特定配置可以在這裡添加
    // 例如：自定義 DataSource、Connection Pool 設定等
}

-- MySQL 資料庫初始化腳本
-- 建立資料庫
CREATE DATABASE IF NOT EXISTS yf_crouse 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 建立使用者（如果尚未存在）
CREATE USER IF NOT EXISTS 'c_user'@'localhost' IDENTIFIED BY 'user0818';

-- 授予權限
GRANT ALL PRIVILEGES ON yf_crouse.* TO 'c_user'@'localhost';
FLUSH PRIVILEGES;

-- 使用資料庫
USE yf_crouse;

-- 注意：表格結構將由 Hibernate 自動建立（spring.jpa.hibernate.ddl-auto=update）

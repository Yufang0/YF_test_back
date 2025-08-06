package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Employee Entity 對應資料庫表 tb_bas_employee
 * <p>
 * 此實體類別代表員工資訊，包含員工的基本資料如員工編號、姓名、郵箱、部門等資訊。
 * 實作 Serializable 介面以支援序列化操作。
 * MySQL 優化版本，包含索引定義和適當的欄位長度設定。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-01-15
 */
@Entity
@Table(name = "tb_bas_employee", indexes = {
        @Index(name = "idx_emp_code", columnList = "emp_code"),
        @Index(name = "idx_dept_id", columnList = "dept_id"),
        @Index(name = "idx_is_active", columnList = "is_active"),
        @Index(name = "idx_create_time", columnList = "create_time")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 員工主鍵 ID，自動遞增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Integer empId;

    /**
     * 員工編號，唯一識別碼
     */
    @Column(name = "emp_code", length = 20, unique = true)
    private String empCode;

    /**
     * 員工姓名，必填欄位
     */
    @Column(name = "emp_name", length = 50, nullable = false)
    private String empName;

    /**
     * 員工電子郵箱，增加長度以符合現代郵箱格式
     */
    @Column(name = "emp_email", length = 100)
    private String empEmail;

    /**
     * 員工連絡電話
     */
    @Column(name = "emp_phone", length = 20)
    private String empPhone;

    /**
     * 部門 ID，外鍵參照 tb_department(id)
     */
    @Column(name = "dept_id")
    private Integer deptId;

    /**
     * 職位名稱
     */
    @Column(name = "job_title", length = 100)
    private String jobTitle;

    /**
     * 入職日期
     */
    @Column(name = "hire_date")
    private LocalDate hireDate;

    /**
     * 離職日期
     */
    @Column(name = "resign_date")
    private LocalDate resignDate;

    /**
     * 員工狀態，1:啟用 0:停用
     * 使用標準的 Boolean 類型
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * 建立時間，自動設定為當前時間
     */
    @Column(name = "create_time", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    /**
     * 建立者
     */
    @Column(name = "create_user", length = 20)
    private String createUser;

    /**
     * 更新時間，自動更新為當前時間
     */
    @Column(name = "update_time")
    @UpdateTimestamp
    private LocalDateTime updateTime;

    /**
     * 更新者
     */
    @Column(name = "update_user", length = 20)
    private String updateUser;
}

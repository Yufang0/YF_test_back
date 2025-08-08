package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 員工資料傳輸物件 (Data Transfer Object)
 * <p>
 * 用於在不同層級之間傳輸員工資料，避免直接暴露 Entity 結構。
 * 包含員工的基本資訊和系統記錄資訊。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-08-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    /**
     * 員工主鍵 ID
     */
    private Integer empId;

    /**
     * 員工編號
     */
    private String empCode;

    /**
     * 員工姓名
     */
    private String empName;

    /**
     * 員工電子郵箱
     */
    private String empEmail;

    /**
     * 員工連絡電話
     */
    private String empPhone;

    /**
     * 部門 ID
     */
    private Integer deptId;

    /**
     * 職位名稱
     */
    private String jobTitle;

    /**
     * 入職日期
     */
    private LocalDate hireDate;

    /**
     * 離職日期
     */
    private LocalDate resignDate;

    /**
     * 員工狀態
     */
    private Boolean isActive;

    /**
     * 建立時間
     */
    private LocalDateTime createTime;

    /**
     * 更新時間
     */
    private LocalDateTime updateTime;
}

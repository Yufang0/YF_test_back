package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("emp_id")
    private Integer empId;

    /**
     * 員工編號
     */
    @JsonProperty("emp_code")
    private String empCode;

    /**
     * 員工姓名
     */
    @JsonProperty("emp_name")
    private String empName;

    /**
     * 員工電子郵箱
     */
    @JsonProperty("emp_email")
    private String empEmail;

    /**
     * 員工連絡電話
     */
    @JsonProperty("emp_phone")
    private String empPhone;

    /**
     * 部門 ID
     */
    @JsonProperty("dept_id")
    private Integer deptId;

    /**
     * 職位名稱
     */
    @JsonProperty("job_title")
    private String jobTitle;

    /**
     * 入職日期
     */
    @JsonProperty("hire_date")
    private LocalDate hireDate;

    /**
     * 離職日期
     */
    @JsonProperty("resign_date")
    private LocalDate resignDate;

    /**
     * 員工狀態
     */
    @JsonProperty("is_active")
    private Boolean isActive;

    /**
     * 建立時間
     */
    @JsonProperty("create_time")
    private LocalDateTime createTime;

    /**
     * 更新時間
     */
    @JsonProperty("update_time")
    private LocalDateTime updateTime;
}

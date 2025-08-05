package com.example.demo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Employee View Object
 * <p>
 * 用於向前端返回員工完整資訊的視圖物件，包含格式化的日期時間欄位。
 * 此 VO 主要用於 API 回應，提供前端展示所需的所有員工資料。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-01-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeVo {

    /**
     * 員工 ID
     */
    @JsonProperty("emp_id")
    private Integer id;

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
     * 部門 ID
     */
    @JsonProperty("dept_id")
    private Integer deptId;

    /**
     * 入職日期
     */
    @JsonProperty("hire_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    /**
     * 員工狀態
     */
    @JsonProperty("is_active")
    private Boolean isActive;

    /**
     * 員工狀態顯示文字
     */
    @JsonProperty("status_text")
    private String statusText;

    /**
     * 建立時間
     */
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 建立者
     */
    @JsonProperty("create_user")
    private String createUser;

    /**
     * 工作天數（從入職日期到當前日期）
     */
    @JsonProperty("working_days")
    private Long workingDays;
}

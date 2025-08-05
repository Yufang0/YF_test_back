package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Employee 摘要 DTO
 * <p>
 * 用於返回員工的簡要資訊，通常用於下拉選單或列表顯示。
 * 僅包含最基本的員工識別資訊，減少資料傳輸量。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-01-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSummaryDto {

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
}

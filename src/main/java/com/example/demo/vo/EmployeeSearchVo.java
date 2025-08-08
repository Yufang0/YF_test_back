package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 員工搜尋 Value Object
 * <p>
 * 用於封裝員工查詢條件，支援動態條件查詢。
 * 所有欄位皆為可選，只有非空值才會被加入查詢條件中。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-08-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSearchVo {

    /**
     * 員工編號
     */
    private String empCode;

    /**
     * 員工姓名
     */
    private String empName;

    /**
     * 部門 ID
     */
    private Integer deptId;

    /**
     * 職位名稱
     */
    private String jobTitle;
}

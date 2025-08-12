package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
 * 部門建立資料傳輸物件
 * 
 * 用於接收前端傳入的部門資料，並進行驗證和轉換。
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCreateDto {

    @JsonProperty("dept_code")
    @NotBlank(message = "部門編號不得為空")
    @Size(max = 20, message = "部門編號長度不得超過 20 個字元")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "部門編號只能包含大寫字母和數字")
    private String deptCode;

    @JsonProperty("dept_name")
    @NotBlank(message = "部門名稱不得為空")
    private String deptName;

    @JsonProperty("parent_dept_id")
    private Integer parentDeptId;

    @JsonProperty("dept_level")
    @Size(max = 10, message = "部門層級長度不得超過 10 個字元")
    private String deptLevel;

    @JsonProperty("manager_emp_id")
    private Integer managerEmpId;

    @JsonProperty("is_active")
    @NotNull(message = "部門是否啟用不得為空")
    private Boolean isActive;

    @JsonProperty("create_user")
    @NotBlank(message = "建立者不得為空")
    @Size(max = 20, message = "建立者長度不得超過 20 個字元")
    private String createUser;

    // Getters and Setters
}

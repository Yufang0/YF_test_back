package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DepartmentUpdateDto {

    @JsonProperty("dept_id")
    @NotNull(message = "部門 ID 不能為空")
    @Positive(message = "部門 ID 必須為正數")
    private Integer deptId;

    @JsonProperty("parent_dept_id")
    private Integer parentDeptId;

    @JsonProperty("dept_code")
    @NotBlank(message = "部門編號不能為空")
    @Size(max = 20, message = "部門編號長度不能超過 20 個字元")
    private String deptCode;

    @JsonProperty("dept_name")
    @NotBlank(message = "部門名稱不能為空")
    @Size(max = 100, message = "部門名稱長度不能超過 100 個字元")
    private String deptName;

    @JsonProperty("dept_level")
    @Size(max = 10, message = "部門層級長度不能超過 10 個字元")
    private String deptLevel;

    @JsonProperty("manager_emp_id")
    private Integer managerEmpId;

    @JsonProperty("is_active")
    @NotNull(message = "部門是否啟用不能為空")
    private Boolean isActive;

    @JsonProperty("update_user")
    @NotBlank(message = "更新者不能為空")
    @Size(max = 20, message = "更新者名稱長度不能超過 20 個字元")
    private String updateUser;
}

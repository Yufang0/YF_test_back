
package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DepartmentDto {
    @JsonProperty("dept_id")
    private Integer deptId;
    @JsonProperty("parent_dept_id")
    private Integer parentDeptId;
    @JsonProperty("dept_code")
    private String deptCode;
    @JsonProperty("dept_name")
    private String deptName;
    @JsonProperty("dept_level")
    private String deptLevel;
    @JsonProperty("manager_emp_id")
    private Integer managerEmpId;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("create_time")
    private LocalDateTime createTime;
    @JsonProperty("create_user")
    private String createUser;
    @JsonProperty("update_time")
    private LocalDateTime updateTime;
    @JsonProperty("update_user")
    private String updateUser;
}

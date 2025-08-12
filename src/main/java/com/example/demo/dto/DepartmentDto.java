package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DepartmentDto {
    private Integer deptId;
    private Integer parentDeptId;
    private String deptCode;
    private String deptName;
    private String deptLevel;
    private Integer managerEmpId;
    private Boolean isActive;
    private LocalDateTime createTime;
    private String createUser;
    private LocalDateTime updateTime;
    private String updateUser;
}

package com.example.demo.vo;

import com.example.demo.system.PageBean;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DepartmentVo extends PageBean {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @JsonProperty("create_user")
    private String createUser;

    @JsonProperty("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;
    
    @JsonProperty("update_user")
    private String updateUser;
}

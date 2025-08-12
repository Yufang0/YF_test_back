package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DepartmentSummaryDto {

    @JsonProperty("dept_id")
    private Integer deptId;

    @JsonProperty("dept_code")
    private String deptCode;

    @JsonProperty("dept_name")
    private String deptName;

}

package com.example.demo.service;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.repo.EmployeeRepository;
import com.example.demo.vo.EmployeeSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 員工服務層範例
 * <p>
 * 展示如何使用新建立的自訂查詢方法
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-08-08
 */
@Service
public class EmployeeQueryService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * 根據員工編號查詢員工資訊（回傳 DTO）
     * 
     * @param empCode 員工編號
     * @return EmployeeDto 或 null
     */
    public EmployeeDto findEmployeeDtoByCode(String empCode) {
        Optional<EmployeeDto> result = employeeRepository.findByEmpCode(empCode);
        return result.orElse(null);
    }

    /**
     * 根據多重條件查詢員工資訊
     * 
     * @param searchVo 搜尋條件
     * @return EmployeeDto 或 null
     */
    public EmployeeDto findEmployeeByConditions(EmployeeSearchVo searchVo) {
        Optional<EmployeeDto> result = employeeRepository.findBySearchConditions(searchVo);
        return result.orElse(null);
    }

    /**
     * 範例：根據部門和職位查詢員工
     * 
     * @param deptId   部門ID
     * @param jobTitle 職位名稱
     * @return EmployeeDto 或 null
     */
    public EmployeeDto findEmployeeByDeptAndJob(Integer deptId, String jobTitle) {
        EmployeeSearchVo searchVo = new EmployeeSearchVo();
        searchVo.setDeptId(deptId);
        searchVo.setJobTitle(jobTitle);

        return findEmployeeByConditions(searchVo);
    }
}

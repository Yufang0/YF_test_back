package com.example.demo.controller;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.service.EmployeeQueryService;
import com.example.demo.service.EmployeeService;
import com.example.demo.vo.EmployeeSearchVo;
import com.example.demo.vo.EmployeeVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 員工查詢控制器
 * <p>
 * 展示如何使用新的自訂查詢功能，包含 DTO 和 VO 兩種回傳格式
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-08-08
 */
@RestController
@RequestMapping("/api/employee-query")
@Tag(name = "員工查詢 API", description = "提供員工資料的查詢功能")
@Slf4j
public class EmployeeQueryController {

    @Autowired
    private EmployeeQueryService employeeQueryService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 根據員工編號查詢員工資訊（回傳 DTO 格式）
     * 
     * @param empCode 員工編號
     * @return EmployeeDto 員工資料傳輸物件
     */
    @GetMapping("/dto/{empCode}")
    @Operation(summary = "查詢員工資訊(DTO)", description = "根據員工編號查詢員工資訊，回傳 DTO 格式")
    public ResponseEntity<EmployeeDto> getEmployeeDto(
            @Parameter(description = "員工編號", example = "EMP001") @PathVariable String empCode) {

        log.info("查詢員工 DTO: {}", empCode);

        EmployeeDto employeeDto = employeeQueryService.findEmployeeDtoByCode(empCode);
        if (employeeDto != null) {
            return ResponseEntity.ok(employeeDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根據員工編號查詢員工資訊（回傳 VO 格式）
     * 
     * @param empCode 員工編號
     * @return EmployeeVo 員工值物件
     */
    @GetMapping("/vo/{empCode}")
    @Operation(summary = "查詢員工資訊(VO)", description = "根據員工編號查詢員工資訊，回傳 VO 格式")
    public ResponseEntity<EmployeeVo> getEmployeeVo(
            @Parameter(description = "員工編號", example = "EMP001") @PathVariable String empCode) {

        log.info("查詢員工 VO: {}", empCode);

        try {
            EmployeeVo employeeVo = employeeService.getEmployeeByCode(empCode);
            return ResponseEntity.ok(employeeVo);
        } catch (Exception e) {
            log.warn("員工不存在: {}", empCode);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根據搜尋條件查詢員工資訊
     * 
     * @param empCode  員工編號（可選）
     * @param empName  員工姓名（可選）
     * @param deptId   部門ID（可選）
     * @param jobTitle 職位名稱（可選）
     * @return EmployeeDto 員工資料傳輸物件
     */
    @GetMapping("/search")
    @Operation(summary = "條件搜尋員工", description = "根據多重條件搜尋員工資訊")
    public ResponseEntity<EmployeeDto> searchEmployee(
            @Parameter(description = "員工編號") @RequestParam(required = false) String empCode,
            @Parameter(description = "員工姓名") @RequestParam(required = false) String empName,
            @Parameter(description = "部門ID") @RequestParam(required = false) Integer deptId,
            @Parameter(description = "職位名稱") @RequestParam(required = false) String jobTitle) {

        log.info("搜尋員工 - 編號: {}, 姓名: {}, 部門: {}, 職位: {}",
                empCode, empName, deptId, jobTitle);

        EmployeeSearchVo searchVo = new EmployeeSearchVo(empCode, empName, deptId, jobTitle);
        EmployeeDto employeeDto = employeeQueryService.findEmployeeByConditions(searchVo);

        if (employeeDto != null) {
            return ResponseEntity.ok(employeeDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 範例：根據部門和職位查詢員工
     * 
     * @param deptId   部門ID
     * @param jobTitle 職位名稱
     * @return EmployeeDto 員工資料傳輸物件
     */
    @GetMapping("/by-dept-job")
    @Operation(summary = "部門職位查詢", description = "根據部門ID和職位名稱查詢員工")
    public ResponseEntity<EmployeeDto> getEmployeeByDeptAndJob(
            @Parameter(description = "部門ID", example = "1") @RequestParam Integer deptId,
            @Parameter(description = "職位名稱", example = "工程師") @RequestParam String jobTitle) {

        log.info("根據部門和職位查詢員工 - 部門: {}, 職位: {}", deptId, jobTitle);

        EmployeeDto employeeDto = employeeQueryService.findEmployeeByDeptAndJob(deptId, jobTitle);

        if (employeeDto != null) {
            return ResponseEntity.ok(employeeDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

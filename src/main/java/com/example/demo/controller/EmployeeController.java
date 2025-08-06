package com.example.demo.controller;

import com.example.demo.dto.EmployeeCreateDto;
import com.example.demo.dto.EmployeeSummaryDto;
import com.example.demo.dto.EmployeeUpdateDto;
import com.example.demo.service.EmployeeService;
import com.example.demo.vo.ApiResponse;
import com.example.demo.vo.EmployeeVo;
import com.example.demo.vo.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Employee Controller 控制器
 * <p>
 * 提供員工相關的 REST API 端點，包含員工的 CRUD 操作、查詢、搜尋等功能。
 * 所有 API 回應都遵循統一的回應格式，並包含適當的 HTTP 狀態碼。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-01-15
 */
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201" }, maxAge = 3600)
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Employee Management", description = "員工管理相關 API")
public class EmployeeController {

        private final EmployeeService employeeService;

        /**
         * 建立新員工
         * 
         * @param createDto 員工建立資料
         * @return 建立成功的員工資訊
         */
        @PostMapping
        @Operation(summary = "建立員工", description = "建立新的員工資料")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "員工建立成功", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "員工編號已存在")
        })
        public ResponseEntity<ApiResponse<EmployeeVo>> createEmployee(
                        @Valid @RequestBody EmployeeCreateDto createDto) {

                log.info("Creating employee: empCode={}", createDto.getEmpCode());

                EmployeeVo employeeVo = employeeService.createEmployee(createDto);
                ApiResponse<EmployeeVo> response = ApiResponse.success(employeeVo, "員工建立成功");

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        /**
         * 根據 ID 查詢員工
         * 
         * @param empId 員工 ID
         * @return 員工資訊
         */
        @GetMapping("/{empId}")
        @Operation(summary = "查詢員工", description = "根據員工 ID 查詢員工資訊")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查詢成功"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "員工不存在")
        })
        public ResponseEntity<ApiResponse<EmployeeVo>> getEmployeeById(
                        @Parameter(description = "員工 ID", required = true) @PathVariable @Min(value = 1, message = "員工 ID 必須大於 0") Integer empId) {

                log.debug("Fetching employee by empId: {}", empId);

                EmployeeVo employeeVo = employeeService.getEmployeeById(empId);
                ApiResponse<EmployeeVo> response = ApiResponse.success(employeeVo);

                return ResponseEntity.ok(response);
        }

        /**
         * 根據員工編號查詢員工
         * 
         * @param empCode 員工編號
         * @return 員工資訊
         */
        @GetMapping("/code/{empCode}")
        @Operation(summary = "根據編號查詢員工", description = "根據員工編號查詢員工資訊")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查詢成功"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "員工編號不存在")
        })
        public ResponseEntity<ApiResponse<EmployeeVo>> getEmployeeByCode(
                        @Parameter(description = "員工編號", required = true) @PathVariable String empCode) {

                log.debug("Fetching employee by code: {}", empCode);

                EmployeeVo employeeVo = employeeService.getEmployeeByCode(empCode);
                ApiResponse<EmployeeVo> response = ApiResponse.success(employeeVo);

                return ResponseEntity.ok(response);
        }

        /**
         * 分頁查詢所有員工
         * 
         * @param page    頁碼（從 0 開始）
         * @param size    每頁大小
         * @param sortBy  排序欄位
         * @param sortDir 排序方向（asc/desc）
         * @return 員工分頁資料
         */
        @GetMapping
        @Operation(summary = "分頁查詢員工", description = "分頁查詢所有員工資料")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查詢成功")
        })
        public ResponseEntity<ApiResponse<PageResponse<EmployeeVo>>> getAllEmployees(
                        @Parameter(description = "頁碼（從 0 開始）", example = "0") @RequestParam(defaultValue = "0") @Min(value = 0, message = "頁碼不能小於 0") Integer page,

                        @Parameter(description = "每頁大小", example = "20") @RequestParam(defaultValue = "20") @Min(value = 1, message = "每頁大小不能小於 1") Integer size,

                        @Parameter(description = "排序欄位", example = "createTime") @RequestParam(defaultValue = "createTime") String sortBy,

                        @Parameter(description = "排序方向", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {

                log.debug("Fetching employees: page={}, size={}, sortBy={}, sortDir={}",
                                page, size, sortBy, sortDir);

                Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();
                Pageable pageable = PageRequest.of(page, size, sort);

                Page<EmployeeVo> employeePage = employeeService.getAllEmployees(pageable);
                PageResponse<EmployeeVo> pageResponse = PageResponse.from(employeePage);
                ApiResponse<PageResponse<EmployeeVo>> response = ApiResponse.success(pageResponse);

                return ResponseEntity.ok(response);
        }

        /**
         * 根據員工狀態分頁查詢員工
         * 
         * @param isActive 員工狀態
         * @param page     頁碼
         * @param size     每頁大小
         * @param sortBy   排序欄位
         * @param sortDir  排序方向
         * @return 員工分頁資料
         */
        @GetMapping("/status/{isActive}")
        @Operation(summary = "根據狀態查詢員工", description = "根據員工狀態分頁查詢員工")
        public ResponseEntity<ApiResponse<PageResponse<EmployeeVo>>> getEmployeesByStatus(
                        @Parameter(description = "員工狀態（true:啟用, false:停用）", required = true) @PathVariable Boolean isActive,

                        @Parameter(description = "頁碼（從 0 開始）", example = "0") @RequestParam(defaultValue = "0") @Min(value = 0, message = "頁碼不能小於 0") Integer page,

                        @Parameter(description = "每頁大小", example = "20") @RequestParam(defaultValue = "20") @Min(value = 1, message = "每頁大小不能小於 1") Integer size,

                        @Parameter(description = "排序欄位", example = "createTime") @RequestParam(defaultValue = "createTime") String sortBy,

                        @Parameter(description = "排序方向", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {

                log.debug("Fetching employees by status: isActive={}, page={}, size={}",
                                isActive, page, size);

                Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();
                Pageable pageable = PageRequest.of(page, size, sort);

                Page<EmployeeVo> employeePage = employeeService.getEmployeesByStatus(isActive, pageable);
                PageResponse<EmployeeVo> pageResponse = PageResponse.from(employeePage);
                ApiResponse<PageResponse<EmployeeVo>> response = ApiResponse.success(pageResponse);

                return ResponseEntity.ok(response);
        }

        /**
         * 根據部門 ID 查詢員工列表
         * 
         * @param deptId 部門 ID
         * @return 員工列表
         */
        @GetMapping("/department/{deptId}")
        @Operation(summary = "查詢部門員工", description = "根據部門 ID 查詢該部門所有員工")
        public ResponseEntity<ApiResponse<List<EmployeeVo>>> getEmployeesByDepartment(
                        @Parameter(description = "部門 ID", required = true) @PathVariable @Min(value = 1, message = "部門 ID 必須大於 0") Integer deptId) {

                log.debug("Fetching employees by department: deptId={}", deptId);

                List<EmployeeVo> employees = employeeService.getEmployeesByDepartment(deptId);
                ApiResponse<List<EmployeeVo>> response = ApiResponse.success(employees);

                return ResponseEntity.ok(response);
        }

        /**
         * 根據姓名模糊搜尋員工
         * 
         * @param empName 員工姓名
         * @return 員工列表
         */
        @GetMapping("/search")
        @Operation(summary = "搜尋員工", description = "根據員工姓名模糊搜尋員工")
        public ResponseEntity<ApiResponse<List<EmployeeVo>>> searchEmployeesByName(
                        @Parameter(description = "員工姓名（支援部分匹配）", required = true) @RequestParam String empName) {

                log.debug("Searching employees by name: {}", empName);

                List<EmployeeVo> employees = employeeService.searchEmployeesByName(empName);
                ApiResponse<List<EmployeeVo>> response = ApiResponse.success(employees);

                return ResponseEntity.ok(response);
        }

        /**
         * 根據職位模糊搜尋員工
         * 
         * @param jobTitle 職位名稱
         * @return 員工列表
         */
        @GetMapping("/search/job-title")
        @Operation(summary = "根據職位搜尋員工", description = "根據職位名稱模糊搜尋員工")
        public ResponseEntity<ApiResponse<List<EmployeeVo>>> searchEmployeesByJobTitle(
                        @Parameter(description = "職位名稱（支援部分匹配）", required = true) @RequestParam String jobTitle) {

                log.debug("Searching employees by job title: {}", jobTitle);

                List<EmployeeVo> employees = employeeService.searchEmployeesByJobTitle(jobTitle);
                ApiResponse<List<EmployeeVo>> response = ApiResponse.success(employees);

                return ResponseEntity.ok(response);
        }

        /**
         * 根據入職日期範圍查詢員工
         * 
         * @param startDate 開始日期
         * @param endDate   結束日期
         * @return 員工列表
         */
        @GetMapping("/hire-date-range")
        @Operation(summary = "根據入職日期查詢員工", description = "根據入職日期範圍查詢員工")
        public ResponseEntity<ApiResponse<List<EmployeeVo>>> getEmployeesByHireDateRange(
                        @Parameter(description = "開始日期", example = "2024-01-01", required = true) @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,

                        @Parameter(description = "結束日期", example = "2024-12-31", required = true) @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

                log.debug("Fetching employees by hire date range: {} to {}", startDate, endDate);

                List<EmployeeVo> employees = employeeService.getEmployeesByHireDateRange(startDate, endDate);
                ApiResponse<List<EmployeeVo>> response = ApiResponse.success(employees);

                return ResponseEntity.ok(response);
        }

        /**
         * 更新員工資訊
         * 
         * @param empId     員工 ID
         * @param updateDto 員工更新資料
         * @return 更新後的員工資訊
         */
        @PutMapping("/{empId}")
        @Operation(summary = "更新員工", description = "更新員工資訊")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "員工不存在"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "員工編號已被其他員工使用")
        })
        public ResponseEntity<ApiResponse<EmployeeVo>> updateEmployee(
                        @Parameter(description = "員工 ID", required = true) @PathVariable @Min(value = 1, message = "員工 ID 必須大於 0") Integer empId,

                        @Valid @RequestBody EmployeeUpdateDto updateDto) {

                log.info("Updating employee: empId={}", empId);

                // 確保 DTO 中的 ID 與路徑參數一致
                updateDto.setEmpId(empId);

                EmployeeVo employeeVo = employeeService.updateEmployee(updateDto);
                ApiResponse<EmployeeVo> response = ApiResponse.success(employeeVo, "員工更新成功");

                return ResponseEntity.ok(response);
        }

        /**
         * 刪除員工
         * 
         * @param empId 員工 ID
         * @return 刪除結果
         */
        @DeleteMapping("/{empId}")
        @Operation(summary = "刪除員工", description = "刪除指定的員工")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "刪除成功"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "員工不存在")
        })
        public ResponseEntity<ApiResponse<Void>> deleteEmployee(
                        @Parameter(description = "員工 ID", required = true) @PathVariable @Min(value = 1, message = "員工 ID 必須大於 0") Integer empId) {

                log.info("Deleting employee: empId={}", empId);

                employeeService.deleteEmployee(empId);
                ApiResponse<Void> response = ApiResponse.success(null, "員工刪除成功");

                return ResponseEntity.ok(response);
        }

        /**
         * 取得部門員工摘要列表
         * 
         * @param deptId 部門 ID
         * @return 員工摘要列表
         */
        @GetMapping("/department/{deptId}/summary")
        @Operation(summary = "取得部門員工摘要", description = "取得指定部門的員工摘要資訊")
        public ResponseEntity<ApiResponse<List<EmployeeSummaryDto>>> getEmployeeSummaryByDepartment(
                        @Parameter(description = "部門 ID", required = true) @PathVariable @Min(value = 1, message = "部門 ID 必須大於 0") Integer deptId) {

                log.debug("Fetching employee summary by department: deptId={}", deptId);

                List<EmployeeSummaryDto> summaries = employeeService.getEmployeeSummaryByDepartment(deptId);
                ApiResponse<List<EmployeeSummaryDto>> response = ApiResponse.success(summaries);

                return ResponseEntity.ok(response);
        }

        /**
         * 統計部門啟用員工數量
         * 
         * @param deptId 部門 ID
         * @return 員工數量
         */
        @GetMapping("/department/{deptId}/count")
        @Operation(summary = "統計部門員工數", description = "統計指定部門的啟用員工數量")
        public ResponseEntity<ApiResponse<Long>> countActiveEmployeesByDepartment(
                        @Parameter(description = "部門 ID", required = true) @PathVariable @Min(value = 1, message = "部門 ID 必須大於 0") Integer deptId) {

                log.debug("Counting active employees by department: deptId={}", deptId);

                long count = employeeService.countActiveEmployeesByDepartment(deptId);
                ApiResponse<Long> response = ApiResponse.success(count);

                return ResponseEntity.ok(response);
        }
}

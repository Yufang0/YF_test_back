package com.example.demo.service;

import com.example.demo.dto.EmployeeCreateDto;
import com.example.demo.dto.EmployeeSummaryDto;
import com.example.demo.dto.EmployeeUpdateDto;
import com.example.demo.entity.EmployeeEntity;
import com.example.demo.exception.EmployeeAlreadyExistsException;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repo.EmployeeRepository;
import com.example.demo.vo.EmployeeVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Employee Service 服務類別
 * <p>
 * 提供員工相關的業務邏輯處理，包含員工的 CRUD 操作、資料驗證、
 * 以及業務規則的實現。此服務層負責協調資料存取層和控制層之間的操作。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-01-15
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * 建立新員工
     * 
     * @param createDto 員工建立資料
     * @return 建立成功的員工 VO
     * @throws EmployeeAlreadyExistsException 當員工編號已存在時
     */
    @Transactional
    public EmployeeVo createEmployee(EmployeeCreateDto createDto) {
        log.info("Starting employee creation: empCode={}", createDto.getEmpCode());

        // 檢查員工編號是否已存在
        if (employeeRepository.existsByEmpCode(createDto.getEmpCode())) {
            log.warn("Employee code already exists: {}", createDto.getEmpCode());
            throw new EmployeeAlreadyExistsException(
                    "員工編號已存在: " + createDto.getEmpCode());
        }

        try {
            EmployeeEntity entity = convertCreateDtoToEntity(createDto);
            entity.setCreateTime(LocalDateTime.now());

            EmployeeEntity savedEntity = employeeRepository.save(entity);
            log.info("Employee created successfully: empId={}, empCode={}",
                    savedEntity.getEmpId(), savedEntity.getEmpCode());

            return convertEntityToVo(savedEntity);
        } catch (Exception e) {
            log.error("Employee creation failed: empCode={}, error={}",
                    createDto.getEmpCode(), e.getMessage(), e);
            throw new RuntimeException("建立員工失敗: " + e.getMessage(), e);
        }
    }

    /**
     * 根據 ID 查詢員工
     * 
     * @param id 員工 ID
     * @return 員工 VO
     * @throws EmployeeNotFoundException 當員工不存在時
     */
    public EmployeeVo getEmployeeById(Integer id) {
        log.debug("Fetching employee by id: {}", id);

        EmployeeEntity entity = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("員工不存在: " + id));

        return convertEntityToVo(entity);
    }

    /**
     * 根據員工編號查詢員工
     * 
     * @param empCode 員工編號
     * @return 員工 VO
     * @throws EmployeeNotFoundException 當員工不存在時
     */
    public EmployeeVo getEmployeeByCode(String empCode) {
        log.debug("Fetching employee by code: {}", empCode);

        EmployeeEntity entity = employeeRepository.findByEmpCode(empCode)
                .orElseThrow(() -> new EmployeeNotFoundException("員工編號不存在: " + empCode));

        return convertEntityToVo(entity);
    }

    /**
     * 分頁查詢所有員工
     * 
     * @param pageable 分頁參數
     * @return 員工分頁資料
     */
    public Page<EmployeeVo> getAllEmployees(Pageable pageable) {
        log.debug("Fetching employees with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<EmployeeEntity> entityPage = employeeRepository.findAll(pageable);
        List<EmployeeVo> voList = entityPage.getContent().stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());

        return new PageImpl<>(voList, pageable, entityPage.getTotalElements());
    }

    /**
     * 根據員工狀態分頁查詢員工
     * 
     * @param isActive 員工狀態
     * @param pageable 分頁參數
     * @return 員工分頁資料
     */
    public Page<EmployeeVo> getEmployeesByStatus(Boolean isActive, Pageable pageable) {
        log.debug("Fetching employees by status: isActive={}, page={}, size={}",
                isActive, pageable.getPageNumber(), pageable.getPageSize());

        Page<EmployeeEntity> entityPage = employeeRepository.findByIsActive(isActive, pageable);
        List<EmployeeVo> voList = entityPage.getContent().stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());

        return new PageImpl<>(voList, pageable, entityPage.getTotalElements());
    }

    /**
     * 根據部門 ID 查詢員工列表
     * 
     * @param deptId 部門 ID
     * @return 員工列表
     */
    public List<EmployeeVo> getEmployeesByDepartment(Integer deptId) {
        log.debug("Fetching employees by department: deptId={}", deptId);

        List<EmployeeEntity> entities = employeeRepository.findByDeptId(deptId);
        return entities.stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());
    }

    /**
     * 根據姓名模糊搜尋員工
     * 
     * @param empName 員工姓名（支援部分匹配）
     * @return 員工列表
     */
    public List<EmployeeVo> searchEmployeesByName(String empName) {
        log.debug("Searching employees by name: {}", empName);

        List<EmployeeEntity> entities = employeeRepository.findByEmpNameContaining(empName);
        return entities.stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());
    }

    /**
     * 根據職位名稱模糊搜尋員工
     * 
     * @param jobTitle 職位名稱（支援部分匹配）
     * @return 員工列表
     */
    public List<EmployeeVo> searchEmployeesByJobTitle(String jobTitle) {
        log.debug("Searching employees by job title: {}", jobTitle);

        List<EmployeeEntity> entities = employeeRepository.findByJobTitleContaining(jobTitle);
        return entities.stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());
    }

    /**
     * 根據入職日期範圍查詢員工
     * 
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 員工列表
     */
    public List<EmployeeVo> getEmployeesByHireDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching employees by hire date range: {} to {}", startDate, endDate);

        List<EmployeeEntity> entities = employeeRepository.findByHireDateBetween(startDate, endDate);
        return entities.stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());
    }

    /**
     * 更新員工資訊
     * 
     * @param updateDto 員工更新資料
     * @return 更新後的員工 VO
     * @throws EmployeeNotFoundException      當員工不存在時
     * @throws EmployeeAlreadyExistsException 當員工編號已被其他員工使用時
     */
    @Transactional
    public EmployeeVo updateEmployee(EmployeeUpdateDto updateDto) {
        log.info("Starting employee update: empId={}", updateDto.getEmpId());

        EmployeeEntity existingEntity = employeeRepository.findById(updateDto.getEmpId())
                .orElseThrow(() -> new EmployeeNotFoundException("員工不存在: " + updateDto.getEmpId()));

        // 檢查員工編號是否被其他員工使用
        if (updateDto.getEmpCode() != null &&
                !updateDto.getEmpCode().equals(existingEntity.getEmpCode())) {

            if (employeeRepository.existsByEmpCode(updateDto.getEmpCode())) {
                log.warn("Employee code already exists: {}", updateDto.getEmpCode());
                throw new EmployeeAlreadyExistsException(
                        "員工編號已被其他員工使用: " + updateDto.getEmpCode());
            }
        }

        try {
            updateEntityFromDto(existingEntity, updateDto);
            EmployeeEntity savedEntity = employeeRepository.save(existingEntity);

            log.info("Employee updated successfully: empId={}, empCode={}",
                    savedEntity.getEmpId(), savedEntity.getEmpCode());

            return convertEntityToVo(savedEntity);
        } catch (Exception e) {
            log.error("Employee update failed: empId={}, error={}",
                    updateDto.getEmpId(), e.getMessage(), e);
            throw new RuntimeException("更新員工失敗: " + e.getMessage(), e);
        }
    }

    /**
     * 刪除員工
     * 
     * @param id 員工 ID
     * @throws EmployeeNotFoundException 當員工不存在時
     */
    @Transactional
    public void deleteEmployee(Integer empId) {
        log.info("Starting employee deletion: empId={}", empId);

        if (!employeeRepository.existsById(empId)) {
            throw new EmployeeNotFoundException("員工不存在: " + empId);
        }

        try {
            employeeRepository.deleteById(empId);
            log.info("Employee deleted successfully: empId={}", empId);
        } catch (Exception e) {
            log.error("Employee deletion failed: empId={}, error={}", empId, e.getMessage(), e);
            throw new RuntimeException("刪除員工失敗: " + e.getMessage(), e);
        }
    }

    /**
     * 取得部門員工摘要列表
     * 
     * @param deptId 部門 ID
     * @return 員工摘要列表
     */
    public List<EmployeeSummaryDto> getEmployeeSummaryByDepartment(Integer deptId) {
        log.debug("Fetching employee summary by department: deptId={}", deptId);

        List<EmployeeEntity> entities = employeeRepository.findByDeptId(deptId);
        return entities.stream()
                .filter(entity -> entity.getIsActive() != null && entity.getIsActive())
                .map(entity -> new EmployeeSummaryDto(entity.getEmpId(), entity.getEmpCode(), entity.getEmpName()))
                .collect(Collectors.toList());
    }

    /**
     * 統計部門啟用員工數量
     * 
     * @param deptId 部門 ID
     * @return 啟用員工數量
     */
    public long countActiveEmployeesByDepartment(Integer deptId) {
        log.debug("Counting active employees by department: deptId={}", deptId);
        return employeeRepository.countByDeptIdAndIsActive(deptId, true);
    }

    /**
     * 將建立 DTO 轉換為實體
     * 
     * @param createDto 建立 DTO
     * @return 實體物件
     */
    private EmployeeEntity convertCreateDtoToEntity(EmployeeCreateDto createDto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setEmpCode(createDto.getEmpCode());
        entity.setEmpName(createDto.getEmpName());
        entity.setEmpEmail(createDto.getEmpEmail());
        entity.setDeptId(createDto.getDeptId());
        entity.setJobTitle(createDto.getJobTitle());
        entity.setHireDate(createDto.getHireDate());
        entity.setResignDate(createDto.getResignDate());
        entity.setIsActive(createDto.getIsActive());
        entity.setCreateUser(createDto.getCreateUser());
        return entity;
    }

    /**
     * 從更新 DTO 更新實體
     * 
     * @param entity    要更新的實體
     * @param updateDto 更新 DTO
     */
    private void updateEntityFromDto(EmployeeEntity entity, EmployeeUpdateDto updateDto) {
        if (updateDto.getEmpCode() != null) {
            entity.setEmpCode(updateDto.getEmpCode());
        }
        if (updateDto.getEmpName() != null) {
            entity.setEmpName(updateDto.getEmpName());
        }
        if (updateDto.getEmpEmail() != null) {
            entity.setEmpEmail(updateDto.getEmpEmail());
        }
        if (updateDto.getEmpPhone() != null) { // 添加缺少的 emp_phone 更新
            entity.setEmpPhone(updateDto.getEmpPhone());
        }
        if (updateDto.getDeptId() != null) {
            entity.setDeptId(updateDto.getDeptId());
        }
        if (updateDto.getJobTitle() != null) {
            entity.setJobTitle(updateDto.getJobTitle());
        }
        if (updateDto.getHireDate() != null) {
            entity.setHireDate(updateDto.getHireDate());
        }
        if (updateDto.getResignDate() != null) {
            entity.setResignDate(updateDto.getResignDate());
        }
        if (updateDto.getIsActive() != null) {
            entity.setIsActive(updateDto.getIsActive());
        }
        if (updateDto.getUpdateUser() != null) { // 添加缺少的 update_user 更新
            entity.setUpdateUser(updateDto.getUpdateUser());
        }
        // 更新時間會由 @UpdateTimestamp 自動處理
    }

    /**
     * 將實體轉換為 VO
     * 
     * @param entity 實體物件
     * @return VO 物件
     */
    private EmployeeVo convertEntityToVo(EmployeeEntity entity) {
        EmployeeVo vo = new EmployeeVo();
        vo.setId(entity.getEmpId());
        vo.setEmpCode(entity.getEmpCode());
        vo.setEmpName(entity.getEmpName());
        vo.setEmpEmail(entity.getEmpEmail());
        vo.setEmpPhone(entity.getEmpPhone()); // 添加缺少的 emp_phone 映射
        vo.setDeptId(entity.getDeptId());
        vo.setJobTitle(entity.getJobTitle());
        vo.setHireDate(entity.getHireDate());
        vo.setResignDate(entity.getResignDate());
        vo.setIsActive(entity.getIsActive());
        vo.setStatusText(entity.getIsActive() != null && entity.getIsActive() ? "啟用" : "停用");
        vo.setCreateTime(entity.getCreateTime());
        vo.setCreateUser(entity.getCreateUser());
        vo.setUpdateTime(entity.getUpdateTime()); // 也添加缺少的 update_time 映射
        vo.setUpdateUser(entity.getUpdateUser()); // 也添加缺少的 update_user 映射

        // 計算工作天數
        if (entity.getHireDate() != null) {
            vo.setWorkingDays(ChronoUnit.DAYS.between(entity.getHireDate(), LocalDate.now()));
        }

        return vo;
    }

    /**
     * 根據綜合條件搜尋員工
     * 使用動態查詢，支援空值過濾
     * 
     * @param empCode  員工編號（可選，部分匹配）
     * @param empName  員工姓名（可選，部分匹配）
     * @param deptId   部門 ID（可選，精確匹配）
     * @param jobTitle 職位（可選，部分匹配）
     * @return 符合條件的員工列表
     */
    public List<EmployeeVo> searchEmployeesByCriteria(String empCode, String empName, Integer deptId, String jobTitle) {
        log.debug("Searching employees by criteria: empCode={}, empName={}, deptId={}, jobTitle={}",
                empCode, empName, deptId, jobTitle);

        List<EmployeeEntity> entities = employeeRepository.findByCriteria(empCode, empName, deptId, jobTitle);
        return entities.stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());
    }
}

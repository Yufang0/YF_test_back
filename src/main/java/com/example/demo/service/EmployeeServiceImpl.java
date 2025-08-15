package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.EmployeeCreateDto;
import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.EmployeeSummaryDto;
import com.example.demo.dto.EmployeeUpdateDto;
import com.example.demo.entity.EmployeeEntity;
import com.example.demo.exception.EmployeeAlreadyExistsException;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repo.EmployeeRepository;
import com.example.demo.util.EntityDtoConverter;
import com.example.demo.vo.EmployeeVo;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of EmployeeService interface
 */
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeDto> findAll() {
        log.info("Finding all employees");
        List<EmployeeEntity> employees = employeeRepository.findAll();
        return EntityDtoConverter.convertList(employees, EmployeeDto.class);
    }

    @Override
    public Optional<EmployeeDto> findById(Integer id) {
        log.info("Finding employee by ID: {}", id);
        Optional<EmployeeEntity> employeeOpt = employeeRepository.findById(id);
        return EntityDtoConverter.convertOptional(employeeOpt, EmployeeDto.class);
    }

    @Override
    public Optional<EmployeeDto> findByEmpCode(String empCode) {
        log.info("Finding employee by employee code: {}", empCode);
        Optional<EmployeeEntity> employeeOpt = employeeRepository.findEntityByEmpCode(empCode);
        return EntityDtoConverter.convertOptional(employeeOpt, EmployeeDto.class);
    }

    @Override
    @Transactional
    public EmployeeDto create(EmployeeVo employeeVo) {
        log.info("Creating new employee: {}", employeeVo.getEmpName());

        // 檢查員工編號是否已存在
        if (employeeVo.getEmpCode() != null &&
                employeeRepository.existsByEmpCode(employeeVo.getEmpCode())) {
            log.warn("Employee code already exists: {}", employeeVo.getEmpCode());
            throw new EmployeeAlreadyExistsException("員工編號已存在: " + employeeVo.getEmpCode());
        }

        // Set creation metadata
        employeeVo.setCreateTime(LocalDateTime.now());
        if (employeeVo.getIsActive() == null) {
            employeeVo.setIsActive(true);
        }

        EmployeeEntity entity = EntityDtoConverter.convert(employeeVo, EmployeeEntity.class);
        EmployeeEntity savedEmployee = employeeRepository.save(entity);
        return EntityDtoConverter.convert(savedEmployee, EmployeeDto.class);
    }

    @Override
    @Transactional
    public EmployeeDto update(Integer id, EmployeeVo employeeVo) {
        log.info("Updating employee ID: {}", id);

        return employeeRepository.findById(id)
                .<EmployeeDto>map(existingEmp -> {
                    // 檢查員工編號是否與其他員工重複（排除自己）
                    if (employeeVo.getEmpCode() != null &&
                            !employeeVo.getEmpCode().equals(existingEmp.getEmpCode()) &&
                            employeeRepository.existsByEmpCode(employeeVo.getEmpCode())) {
                        log.warn("Employee code already exists during update: {}", employeeVo.getEmpCode());
                        throw new EmployeeAlreadyExistsException("員工編號已被其他員工使用: " + employeeVo.getEmpCode());
                    }

                    // Update fields using custom mapping
                    EmployeeEntity updatedEmp = EntityDtoConverter.convert(employeeVo, EmployeeEntity.class);
                    updatedEmp.setEmpId(existingEmp.getEmpId());
                    updatedEmp.setUpdateTime(LocalDateTime.now());
                    existingEmp = updatedEmp;

                    // Save the updated entity
                    updatedEmp = employeeRepository.save(existingEmp);
                    return EntityDtoConverter.convert(updatedEmp, EmployeeDto.class);
                })
                .orElseThrow(() -> {
                    log.error("Employee not found with ID: {}", id);
                    return new EmployeeNotFoundException("員工不存在: " + id);
                });
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        log.info("Deleting employee ID: {}", id);
        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeDto> findByPage(EmployeeVo vo) {
        log.info("Finding employees by criteria with pagination");
        // Since findByPage doesn't exist in repository, we'll implement basic search
        List<EmployeeEntity> entities;

        if (vo.getDeptId() != null) {
            entities = employeeRepository.findByDeptId(vo.getDeptId());
        } else if (vo.getEmpName() != null && !vo.getEmpName().trim().isEmpty()) {
            entities = employeeRepository.findByEmpNameContaining(vo.getEmpName());
        } else {
            entities = employeeRepository.findAll();
        }

        return EntityDtoConverter.convertList(entities, EmployeeDto.class);
    }

    @Override
    public Page<EmployeeDto> findAll(Pageable pageable) {
        log.info("Finding all employees with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<EmployeeEntity> entityPage = employeeRepository.findAll(pageable);
        List<EmployeeDto> dtoList = EntityDtoConverter.convertList(entityPage.getContent(), EmployeeDto.class);

        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }

    @Override
    public List<EmployeeDto> findByDepartment(Integer deptId) {
        log.info("Finding employees by department ID: {}", deptId);
        List<EmployeeEntity> employees = employeeRepository.findByDeptId(deptId);
        return EntityDtoConverter.convertList(employees, EmployeeDto.class);
    }

    @Override
    public Page<EmployeeDto> findByStatus(Boolean isActive, Pageable pageable) {
        log.info("Finding employees by status: isActive={}, page={}, size={}",
                isActive, pageable.getPageNumber(), pageable.getPageSize());

        Page<EmployeeEntity> entityPage = employeeRepository.findByIsActive(isActive, pageable);
        List<EmployeeDto> dtoList = EntityDtoConverter.convertList(entityPage.getContent(), EmployeeDto.class);

        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }

    // Legacy method implementations for backward compatibility

    @Override
    @Transactional
    public EmployeeVo createEmployee(EmployeeCreateDto createDto) {
        log.info("Starting employee creation: empCode={}", createDto.getEmpCode());

        // 檢查員工編號是否已存在
        if (employeeRepository.existsByEmpCode(createDto.getEmpCode())) {
            log.warn("Employee code already exists: {}", createDto.getEmpCode());
            throw new EmployeeAlreadyExistsException("員工編號已存在: " + createDto.getEmpCode());
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

    @Override
    public EmployeeVo getEmployeeById(Integer id) {
        log.debug("Fetching employee by id: {}", id);

        EmployeeEntity entity = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("員工不存在: " + id));

        return convertEntityToVo(entity);
    }

    @Override
    public EmployeeVo getEmployeeByCode(String empCode) {
        log.debug("Fetching employee by code: {}", empCode);

        EmployeeEntity entity = employeeRepository.findEntityByEmpCode(empCode)
                .orElseThrow(() -> new EmployeeNotFoundException("員工編號不存在: " + empCode));

        return convertEntityToVo(entity);
    }

    @Override
    public Page<EmployeeVo> getAllEmployees(Pageable pageable) {
        log.info("Fetching employees with pagination: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<EmployeeEntity> entityPage = employeeRepository.findAll(pageable);
        log.info("Found {} employees in page {}/{}, total elements: {}",
                entityPage.getNumberOfElements(), entityPage.getNumber() + 1,
                entityPage.getTotalPages(), entityPage.getTotalElements());

        List<EmployeeVo> voList = entityPage.getContent().stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());

        return new PageImpl<>(voList, pageable, entityPage.getTotalElements());
    }

    @Override
    public Page<EmployeeVo> getEmployeesByStatus(Boolean isActive, Pageable pageable) {
        log.debug("Fetching employees by status: isActive={}, page={}, size={}",
                isActive, pageable.getPageNumber(), pageable.getPageSize());

        Page<EmployeeEntity> entityPage = employeeRepository.findByIsActive(isActive, pageable);
        List<EmployeeVo> voList = entityPage.getContent().stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());

        return new PageImpl<>(voList, pageable, entityPage.getTotalElements());
    }

    @Override
    public List<EmployeeVo> getEmployeesByDepartment(Integer deptId) {
        log.debug("Fetching employees by department: deptId={}", deptId);

        List<EmployeeEntity> entities = employeeRepository.findByDeptId(deptId);
        return entities.stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeVo> searchEmployeesByName(String empName) {
        log.debug("Searching employees by name: {}", empName);

        List<EmployeeEntity> entities = employeeRepository.findByEmpNameContaining(empName);
        return entities.stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeVo> searchEmployeesByJobTitle(String jobTitle) {
        log.debug("Searching employees by job title: {}", jobTitle);

        List<EmployeeEntity> entities = employeeRepository.findByJobTitleContaining(jobTitle);
        return entities.stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeVo> getEmployeesByHireDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching employees by hire date range: {} to {}", startDate, endDate);

        List<EmployeeEntity> entities = employeeRepository.findByHireDateBetween(startDate, endDate);
        return entities.stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());
    }

    @Override
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

    @Override
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

    @Override
    public List<EmployeeSummaryDto> getEmployeeSummaryByDepartment(Integer deptId) {
        log.debug("Fetching employee summary by department: deptId={}", deptId);

        List<EmployeeEntity> entities = employeeRepository.findByDeptId(deptId);
        return entities.stream()
                .filter(entity -> entity.getIsActive() != null && entity.getIsActive())
                .map(entity -> new EmployeeSummaryDto(entity.getEmpId(), entity.getEmpCode(), entity.getEmpName()))
                .collect(Collectors.toList());
    }

    @Override
    public long countActiveEmployeesByDepartment(Integer deptId) {
        log.debug("Counting active employees by department: deptId={}", deptId);
        return employeeRepository.countByDeptIdAndIsActive(deptId, true);
    }

    @Override
    public List<EmployeeVo> searchEmployeesByCriteria(String empCode, String empName, Integer deptId, String jobTitle) {
        log.debug("Searching employees by criteria: empCode={}, empName={}, deptId={}, jobTitle={}",
                empCode, empName, deptId, jobTitle);

        List<EmployeeEntity> entities = employeeRepository.findByCriteria(empCode, empName, deptId, jobTitle);
        return entities.stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());
    }

    @Override
    public Page<EmployeeVo> searchEmployeesByCriteriaWithPagination(String empCode, String empName, Integer deptId,
            String jobTitle, Pageable pageable) {
        log.debug(
                "Searching employees by criteria with pagination: empCode={}, empName={}, deptId={}, jobTitle={}, page={}, size={}",
                empCode, empName, deptId, jobTitle, pageable.getPageNumber(), pageable.getPageSize());

        Page<EmployeeEntity> entityPage = employeeRepository.findByCriteriaWithPagination(empCode, empName, deptId,
                jobTitle, pageable);
        List<EmployeeVo> voList = entityPage.getContent().stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());

        return new PageImpl<>(voList, pageable, entityPage.getTotalElements());
    }

    // Helper methods for entity/DTO/VO conversion

    private EmployeeEntity convertCreateDtoToEntity(EmployeeCreateDto createDto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setEmpCode(createDto.getEmpCode());
        entity.setEmpName(createDto.getEmpName());
        entity.setEmpEmail(createDto.getEmpEmail());
        entity.setEmpPhone(createDto.getEmpPhone());
        entity.setDeptId(createDto.getDeptId());
        entity.setJobTitle(createDto.getJobTitle());
        entity.setHireDate(createDto.getHireDate());
        entity.setResignDate(createDto.getResignDate());
        entity.setIsActive(createDto.getIsActive());
        entity.setCreateUser(createDto.getCreateUser());
        return entity;
    }

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
        if (updateDto.getEmpPhone() != null) {
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
        if (updateDto.getUpdateUser() != null) {
            entity.setUpdateUser(updateDto.getUpdateUser());
        }
    }

    private EmployeeVo convertEntityToVo(EmployeeEntity entity) {
        EmployeeVo vo = new EmployeeVo();
        vo.setEmpId(entity.getEmpId());
        vo.setEmpCode(entity.getEmpCode());
        vo.setEmpName(entity.getEmpName());
        vo.setEmpEmail(entity.getEmpEmail());
        vo.setEmpPhone(entity.getEmpPhone());
        vo.setDeptId(entity.getDeptId());
        vo.setJobTitle(entity.getJobTitle());
        vo.setHireDate(entity.getHireDate());
        vo.setResignDate(entity.getResignDate());
        vo.setIsActive(entity.getIsActive());
        vo.setStatusText(entity.getIsActive() != null && entity.getIsActive() ? "啟用" : "停用");
        vo.setCreateTime(entity.getCreateTime());
        vo.setCreateUser(entity.getCreateUser());
        vo.setUpdateTime(entity.getUpdateTime());
        vo.setUpdateUser(entity.getUpdateUser());

        // 計算工作天數
        if (entity.getHireDate() != null) {
            vo.setWorkingDays(ChronoUnit.DAYS.between(entity.getHireDate(), LocalDate.now()));
        }

        return vo;
    }
}

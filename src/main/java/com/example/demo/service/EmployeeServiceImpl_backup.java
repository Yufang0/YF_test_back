package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.EmployeeEntity;
import com.example.demo.exception.EmployeeAlreadyExistsException;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repo.EmployeeRepository;
import com.example.demo.util.EntityDtoConverter;
import com.example.demo.vo.EmployeeVo;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of EmployeeService interface (OLD VERSION - BACKUP)
 */
@Service("employeeServiceImplBackup")
@Slf4j
public class EmployeeServiceImplBackup implements EmployeeService {

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
            .map(existingEmp -> {
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
}

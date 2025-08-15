package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.EmployeeCreateDto;
import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.EmployeeSummaryDto;
import com.example.demo.dto.EmployeeUpdateDto;
import com.example.demo.vo.EmployeeVo;

/**
 * Service interface for Employee operations
 */
@Service
public interface EmployeeService {

    
    /**
     * Find all employees
     * 
     * @return list of all employees
     */
    List<EmployeeDto> findAll();

    /**
     * Find employee by ID
     * 
     * @param id employee ID
     * @return optional containing employee if found
     */
    Optional<EmployeeDto> findById(Integer id);

    /**
     * Find employee by employee code
     * 
     * @param empCode employee code
     * @return optional containing employee if found
     */
    Optional<EmployeeDto> findByEmpCode(String empCode);

    /**
     * Create a new employee
     * 
     * @param employee employee entity to save
     * @return saved employee entity
     */
    EmployeeDto create(EmployeeVo employee);

    /**
     * Update an existing employee
     * 
     * @param id employee ID to update
     * @param employee updated employee data
     * @return updated employee entity
     */
    EmployeeDto update(Integer id, EmployeeVo employee);

    /**
     * Delete an employee by ID
     * 
     * @param id employee ID to delete
     */
    void deleteById(Integer id);

    /**
     * Find employees by search criteria with pagination
     * 
     * @param vo search criteria
     * @return list of employee DTOs
     */
    List<EmployeeDto> findByPage(EmployeeVo vo);

    /**
     * Find employees by page with pagination
     * 
     * @param pageable pagination parameters
     * @return page of employee DTOs
     */
    Page<EmployeeDto> findAll(Pageable pageable);

    /**
     * Find employees by department ID
     * 
     * @param deptId department ID
     * @return list of employees in the department
     */
    List<EmployeeDto> findByDepartment(Integer deptId);

    /**
     * Find employees by status with pagination
     * 
     * @param isActive employee status
     * @param pageable pagination parameters
     * @return page of employee DTOs
     */
    Page<EmployeeDto> findByStatus(Boolean isActive, Pageable pageable);

    // Legacy method names for backward compatibility with existing controller
    
    /**
     * Create a new employee (legacy method name)
     * 
     * @param createDto employee creation data
     * @return created employee VO
     */
    EmployeeVo createEmployee(EmployeeCreateDto createDto);

    /**
     * Get employee by ID (legacy method name)
     * 
     * @param id employee ID
     * @return employee VO
     */
    EmployeeVo getEmployeeById(Integer id);

    /**
     * Get employee by code (legacy method name)
     * 
     * @param empCode employee code
     * @return employee VO
     */
    EmployeeVo getEmployeeByCode(String empCode);

    /**
     * Get all employees with pagination (legacy method name)
     * 
     * @param pageable pagination parameters
     * @return page of employee VOs
     */
    Page<EmployeeVo> getAllEmployees(Pageable pageable);

    /**
     * Get employees by status (legacy method name)
     * 
     * @param isActive employee status
     * @param pageable pagination parameters
     * @return page of employee VOs
     */
    Page<EmployeeVo> getEmployeesByStatus(Boolean isActive, Pageable pageable);

    /**
     * Get employees by department (legacy method name)
     * 
     * @param deptId department ID
     * @return list of employee VOs
     */
    List<EmployeeVo> getEmployeesByDepartment(Integer deptId);

    /**
     * Search employees by name (legacy method name)
     * 
     * @param empName employee name
     * @return list of employee VOs
     */
    List<EmployeeVo> searchEmployeesByName(String empName);

    /**
     * Search employees by job title (legacy method name)
     * 
     * @param jobTitle job title
     * @return list of employee VOs
     */
    List<EmployeeVo> searchEmployeesByJobTitle(String jobTitle);

    /**
     * Get employees by hire date range (legacy method name)
     * 
     * @param startDate start date
     * @param endDate end date
     * @return list of employee VOs
     */
    List<EmployeeVo> getEmployeesByHireDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Update employee (legacy method name)
     * 
     * @param updateDto employee update data
     * @return updated employee VO
     */
    EmployeeVo updateEmployee(EmployeeUpdateDto updateDto);

    /**
     * Delete employee (legacy method name)
     * 
     * @param empId employee ID
     */
    void deleteEmployee(Integer empId);

    /**
     * Get employee summary by department (legacy method name)
     * 
     * @param deptId department ID
     * @return list of employee summaries
     */
    List<EmployeeSummaryDto> getEmployeeSummaryByDepartment(Integer deptId);

    /**
     * Count active employees by department (legacy method name)
     * 
     * @param deptId department ID
     * @return count of active employees
     */
    long countActiveEmployeesByDepartment(Integer deptId);

    /**
     * Search employees by criteria (legacy method name)
     * 
     * @param empCode employee code
     * @param empName employee name
     * @param deptId department ID
     * @param jobTitle job title
     * @return list of employee VOs
     */
    List<EmployeeVo> searchEmployeesByCriteria(String empCode, String empName, Integer deptId, String jobTitle);

    /**
     * Search employees by criteria with pagination (legacy method name)
     * 
     * @param empCode employee code
     * @param empName employee name
     * @param deptId department ID
     * @param jobTitle job title
     * @param pageable pagination parameters
     * @return page of employee VOs
     */
    Page<EmployeeVo> searchEmployeesByCriteriaWithPagination(String empCode, String empName, Integer deptId, String jobTitle, Pageable pageable);
}

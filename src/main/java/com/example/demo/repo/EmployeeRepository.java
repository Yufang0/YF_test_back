package com.example.demo.repo;

import com.example.demo.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Employee Repository 介面
 * <p>
 * 提供員工資料的資料存取層操作，包含基本的 CRUD 操作以及自定義查詢方法。
 * 繼承 JpaRepository 以獲得標準的資料庫操作功能。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-01-15
 */
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    /**
     * 根據員工編號查詢員工資訊
     * 
     * @param empCode 員工編號
     * @return 員工實體物件（Optional 包裝）
     */
    Optional<EmployeeEntity> findByEmpCode(String empCode);

    /**
     * 根據員工姓名查詢員工列表（模糊搜尋）
     * 
     * @param empName 員工姓名（支援部分匹配）
     * @return 符合條件的員工列表
     */
    List<EmployeeEntity> findByEmpNameContaining(String empName);

    /**
     * 根據職位名稱查詢員工列表（模糊搜尋）
     * 
     * @param jobTitle 職位名稱（支援部分匹配）
     * @return 符合條件的員工列表
     */
    List<EmployeeEntity> findByJobTitleContaining(String jobTitle);

    /**
     * 根據部門 ID 查詢該部門所有員工
     * 
     * @param deptId 部門 ID
     * @return 該部門的員工列表
     */
    List<EmployeeEntity> findByDeptId(Integer deptId);

    /**
     * 根據員工狀態查詢員工列表（分頁）
     * 
     * @param isActive 員工狀態（true:啟用, false:停用）
     * @param pageable 分頁參數
     * @return 分頁員工資料
     */
    Page<EmployeeEntity> findByIsActive(Boolean isActive, Pageable pageable);

    /**
     * 根據入職日期範圍查詢員工
     * 
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 符合日期範圍的員工列表
     */
    List<EmployeeEntity> findByHireDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 查詢指定部門的啟用員工數量
     * 
     * @param deptId   部門 ID
     * @param isActive 員工狀態
     * @return 符合條件的員工數量
     */
    long countByDeptIdAndIsActive(Integer deptId, Boolean isActive);

    /**
     * 根據員工編號檢查是否存在
     * 
     * @param empCode 員工編號
     * @return true 如果存在，false 如果不存在
     */
    boolean existsByEmpCode(String empCode);

    /**
     * 根據電子郵箱查詢員工（排除指定 ID）
     * 用於更新時檢查郵箱唯一性
     * 
     * @param empEmail 員工電子郵箱
     * @param id       要排除的員工 ID
     * @return 員工實體物件（Optional 包裝）
     */
    @Query("SELECT e FROM EmployeeEntity e WHERE e.empEmail = :empEmail AND e.id != :id")
    Optional<EmployeeEntity> findByEmpEmailAndIdNot(@Param("empEmail") String empEmail, @Param("id") Integer id);
}

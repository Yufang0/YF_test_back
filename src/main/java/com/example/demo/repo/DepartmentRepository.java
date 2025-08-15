package com.example.demo.repo;

import com.example.demo.entity.DepartmentEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Department Repository 介面
 * <p> 
 * 提供部門資料的資料存取層操作，包含基本的 CRUD 操作以及自定義查詢方法。
 * 繼承 JpaRepository 以獲得標準的資料庫操作功能。
 * 繼承 DepartmentRepositoryCustom 以獲得自訂查詢功能。
 * </p>
 * 
 * 主介面要 extends Custom 介面，這樣你在 Service 注入 DepartmentRepository 時，同時擁有 CRUD 與自訂方法。
 */


@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Integer>, DepartmentRepositoryCustom {

    // 根據部門編號查詢部門
    // 因為部門編號是唯一的，所以返回值是 Optional<DepartmentEntity> 查單一資料
    Optional<DepartmentEntity> findByDeptCode(String deptCode);

    // 檢查部門編號是否存在
    boolean existsByDeptCode(String deptCode);

}

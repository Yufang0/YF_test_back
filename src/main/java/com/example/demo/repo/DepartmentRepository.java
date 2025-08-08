package com.example.demo.repo;

import com.example.demo.entity.DepartmentEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Integer> {

    // 根據部門編號查詢部門
    // 因為部門編號是唯一的，所以返回值是 Optional<DepartmentEntity> 查單一資料
    Optional<DepartmentEntity> findByDeptCode(String deptCode);

}

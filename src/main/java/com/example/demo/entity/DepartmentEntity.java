package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity

// Department Entity 對應資料庫表 tb_bas_department
// @Indexes 用於加速查詢性能，包含部門編號、名稱和層級的索引
@Table(name = "tb_bas_department", indexes = {
        @Index(name = "idx_parent_dept_id", columnList = "parent_dept_id"),
        @Index(name = "idx_dept_code", columnList = "dept_code"),
        @Index(name = "idx_dept_name", columnList = "dept_name"),
        @Index(name = "idx_dept_level", columnList = "dept_level")
})

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class DepartmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Integer deptId;

    @Column(name = "parent_dept_id")
    private Integer parentDeptId;

    @Column(name = "dept_code", length = 20, unique = true)
    private String deptCode;

    @Column(name = "dept_name", length = 100, nullable = false)
    private String deptName;

    @Column(name = "dept_level", length = 10)
    private String deptLevel;

    @Column(name = "manager_emp_id")
    private Integer managerEmpId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "create_time", updatable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @Column(name = "create_user", length = 20, nullable = false)
    private String createUser;

    @Column(name = "update_time")
    @UpdateTimestamp
    private LocalDateTime updateTime;

    @Column(name = "update_user", length = 20)
    private String updateUser;
}

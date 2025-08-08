package com.example.demo.repo;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.vo.EmployeeSearchVo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 員工自訂 Repository 實作類別
 * <p>
 * 實作 EmployeeRepositoryCustom 介面，提供使用 EntityManager 和 JPQL 的自訂查詢功能。
 * 支援動態條件組合，只有非空欄位才會被加入查詢條件中。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-08-08
 */
@Repository
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根據員工編號查詢員工資訊
     * 
     * @param empCode 員工編號
     * @return Optional&lt;EmployeeDto&gt; 員工資料傳輸物件
     */
    @Override
    public Optional<EmployeeDto> findByEmpCode(String empCode) {
        if (!StringUtils.hasText(empCode)) {
            return Optional.empty();
        }

        EmployeeSearchVo searchVo = new EmployeeSearchVo();
        searchVo.setEmpCode(empCode);

        return findBySearchConditions(searchVo);
    }

    /**
     * 根據搜尋條件查詢員工資訊
     * <p>
     * 使用 JPQL 動態建構查詢語句，支援多重條件組合。
     * 查詢條件包含：員工編號、員工姓名、部門ID、職位名稱
     * </p>
     * 
     * @param searchVo 員工搜尋條件
     * @return Optional&lt;EmployeeDto&gt; 員工資料傳輸物件
     */
    @Override
    public Optional<EmployeeDto> findBySearchConditions(EmployeeSearchVo searchVo) {
        if (searchVo == null) {
            return Optional.empty();
        }

        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT new com.example.demo.dto.EmployeeDto(");
        jpql.append("e.empId, e.empCode, e.empName, e.empEmail, e.empPhone, ");
        jpql.append("e.deptId, e.jobTitle, e.hireDate, e.resignDate, ");
        jpql.append("e.isActive, e.createTime, e.updateTime) ");
        jpql.append("FROM EmployeeEntity e WHERE 1=1");

        List<Object> parameters = new ArrayList<>();
        int paramIndex = 1;

        // 動態新增查詢條件
        if (StringUtils.hasText(searchVo.getEmpCode())) {
            jpql.append(" AND e.empCode = ?").append(paramIndex);
            parameters.add(searchVo.getEmpCode());
            paramIndex++;
        }

        if (StringUtils.hasText(searchVo.getEmpName())) {
            jpql.append(" AND e.empName LIKE ?").append(paramIndex);
            parameters.add("%" + searchVo.getEmpName() + "%");
            paramIndex++;
        }

        if (searchVo.getDeptId() != null) {
            jpql.append(" AND e.deptId = ?").append(paramIndex);
            parameters.add(searchVo.getDeptId());
            paramIndex++;
        }

        if (StringUtils.hasText(searchVo.getJobTitle())) {
            jpql.append(" AND e.jobTitle LIKE ?").append(paramIndex);
            parameters.add("%" + searchVo.getJobTitle() + "%");
            paramIndex++;
        }

        // 只查詢啟用的員工
        jpql.append(" AND e.isActive = true");

        try {
            TypedQuery<EmployeeDto> query = entityManager.createQuery(jpql.toString(), EmployeeDto.class);

            // 設定參數
            for (int i = 0; i < parameters.size(); i++) {
                query.setParameter(i + 1, parameters.get(i));
            }

            EmployeeDto result = query.getSingleResult();
            return Optional.of(result);

        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            // 記錄異常但不拋出，回傳空結果
            System.err.println("查詢員工資料時發生錯誤: " + e.getMessage());
            return Optional.empty();
        }
    }
}

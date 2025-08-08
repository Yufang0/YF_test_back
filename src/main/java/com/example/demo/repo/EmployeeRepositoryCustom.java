package com.example.demo.repo;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.vo.EmployeeSearchVo;

import java.util.Optional;

/**
 * 員工自訂 Repository 介面
 * <p>
 * 定義員工資料存取層的自訂查詢方法。
 * 此介面將由 EmployeeRepositoryImpl 實作，提供彈性的查詢功能。
 * </p>
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-08-08
 */
public interface EmployeeRepositoryCustom {

    /**
     * 根據員工編號查詢員工資訊
     * <p>
     * 使用 JPQL 動態查詢，透過 EmployeeSearchVo 傳入查詢條件。
     * 只有非空欄位才會被加入查詢條件中，實現彈性的條件查詢。
     * </p>
     * 
     * @param empCode 員工編號
     * @return Optional&lt;EmployeeDto&gt; 員工資料傳輸物件，若未找到則回傳空的 Optional
     */
    Optional<EmployeeDto> findByEmpCode(String empCode);

    /**
     * 根據搜尋條件查詢員工資訊
     * <p>
     * 使用 JPQL 動態查詢，支援多重條件組合查詢。
     * 查詢條件包含：員工編號、員工姓名、部門ID、職位名稱
     * </p>
     * 
     * @param searchVo 員工搜尋條件
     * @return Optional&lt;EmployeeDto&gt; 員工資料傳輸物件，若未找到則回傳空的 Optional
     */
    Optional<EmployeeDto> findBySearchConditions(EmployeeSearchVo searchVo);
}

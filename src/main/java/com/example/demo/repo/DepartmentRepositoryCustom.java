package com.example.demo.repo;

import java.util.List;

import com.example.demo.dto.DepartmentDto;
import com.example.demo.vo.DepartmentVo;

/*
 * 自訂部門 Repository 介面
 * <p>
 * 提供部門資料的自訂查詢方法
 * </p>
 */ 
public interface DepartmentRepositoryCustom {

    List<DepartmentDto> findByPage(DepartmentVo vo);
    
}

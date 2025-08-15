package com.example.demo.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.dto.DepartmentDto;
import com.example.demo.system.BaseRepository;
import com.example.demo.vo.DepartmentVo;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of DepartmentRepositoryCustom
 */
@Repository
@Slf4j
public class DepartmentRepositoryImpl extends BaseRepository implements DepartmentRepositoryCustom {

        @Override
        public List<DepartmentDto> findByPage(DepartmentVo vo) {
                List<Object> args = new ArrayList<>();

                // 添加調試日誌
                log.debug("DepartmentRepositoryImpl.findByPage() - Received vo: {}", vo);
                if (vo != null) {
                        log.debug("Pagination info - firstIndexInPage: {}, lastIndexInPage: {}, pageSize: {}, totalRecords: {}",
                                        vo.getFirstIndexInPage(), vo.getLastIndexInPage(), vo.getPageSize(),
                                        vo.getTotalRecords());
                        log.debug("Sort info - sortColumn: {}, sortDirection: {}", vo.getSortColumn(),
                                        vo.getSortDirection());
                }

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT d.*, ");
                sql.append("pd.dept_name as parent_dept_name, ");
                sql.append("pd.dept_code as parent_dept_code, ");
                sql.append("e.emp_name as manager_name "); // Assuming there's an employee table with emp_name field
                sql.append("FROM tb_bas_department d ");
                sql.append("LEFT JOIN tb_bas_department pd ON d.parent_dept_id = pd.dept_id ");
                sql.append("LEFT JOIN tb_bas_employee e ON d.manager_emp_id = e.emp_id ");
                sql.append("WHERE 1=1 ");

                // Add conditions based on the provided search criteria
                if (vo != null) {
                        if (vo.getDeptId() != null) {
                                sql.append("AND d.dept_id = ? ");
                                args.add(vo.getDeptId());
                        }

                        if (vo.getParentDeptId() != null) {
                                sql.append("AND d.parent_dept_id = ? ");
                                args.add(vo.getParentDeptId());
                        }

                        if (vo.getDeptCode() != null && !vo.getDeptCode().isEmpty()) {
                                sql.append("AND d.dept_code LIKE ? ");
                                args.add("%" + vo.getDeptCode() + "%");
                        }

                        if (vo.getDeptName() != null && !vo.getDeptName().isEmpty()) {
                                sql.append("AND d.dept_name LIKE ? ");
                                args.add("%" + vo.getDeptName() + "%");
                        }

                        if (vo.getDeptLevel() != null && !vo.getDeptLevel().isEmpty()) {
                                sql.append("AND d.dept_level = ? ");
                                args.add(vo.getDeptLevel());
                        }

                        if (vo.getIsActive() != null) {
                                sql.append("AND d.is_active = ? ");
                                args.add(vo.getIsActive());
                        }
                }

                log.debug("Constructed SQL: {}", sql.toString());
                log.debug("Query parameters: {}", args);
                try {
                        return super.executeQryListByPage(DepartmentDto.class, sql.toString(), vo, args);
                } catch (Exception e) {
                        log.error("Error executing department search query", e);
                        return new ArrayList<>();
                }
        }
}

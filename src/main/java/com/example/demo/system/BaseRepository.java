package com.example.demo.system;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseRepository {

    @Resource
    protected JdbcTemplate jdbcTemplate;

    /**
     * 
     * @param sql
     * @param args
     * @return
     */
    protected int countQuery(String sql, List<Object> args) {
        return countQuery(sql, list2Arr(args));
    }

    /**
     * 
     * @param sql
     * @param args
     * @return
     */
    protected int countQuery(String sql, Object... args) {
        String countSql = "SELECT COUNT(*) as totalRow FROM (" + sql + ") t";
        Integer result = jdbcTemplate.queryForObject(countSql, args, Integer.class);
        return result != null ? result : 0;
    }

    /**
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    protected Object executeQrySingleData(String sql, String columnName, List<Object> args) throws Exception {
        return executeQrySingleData(sql, columnName, list2Arr(args));
    }

    /**
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    protected Object executeQrySingleData(String sql, String columnName, Object... args) throws Exception {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, args);
        if (sqlRowSet.next()) {
            return sqlRowSet.getObject(columnName);
        }
        return null;
    }

    /**
     * 
     * @param clazz
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    protected <T> T executeQrySingleData(Class<T> clazz, String sql, List<Object> args) throws Exception {
        return executeQrySingleData(clazz, sql, list2Arr(args));
    }

    /**
     * 
     * @param clazz
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    protected <T> T executeQrySingleData(Class<T> clazz, String sql, Object... args) throws Exception {
        T rtn = null;
        try {
            rtn = jdbcTemplate.queryForObject(sql, args, new UBeanPropertyRowMapper<T>(clazz));
        } catch (EmptyResultDataAccessException e) {
            log.info("No Result!");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return rtn;
    }

    /**
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    protected List<Map<String, Object>> executeQryList(String sql, List<Object> args) throws Exception {
        return executeQryList(sql, list2Arr(args));
    }

    /**
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    protected List<Map<String, Object>> executeQryList(String sql, Object... args) throws Exception {
        return jdbcTemplate.queryForList(sql, args);
    }

    /**
     * 
     * @param clazz
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    protected <T> List<T> executeQryList(Class<T> clazz, String sql, List<Object> args) throws Exception {
        return executeQryList(clazz, sql, list2Arr(args));
    }

    /**
     * 
     * @param clazz
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    protected <T> List<T> executeQryList(Class<T> clazz, String sql, Object... args) throws Exception {
        return jdbcTemplate.query(sql, args, new UBeanPropertyRowMapper<T>(clazz));
    }

    /**
     * 
     * @param clazz
     * @param sql
     * @param pageBean
     * @param args
     * @return
     * @throws Exception
     */
    protected <T> List<T> executeQryListByPage(Class<T> clazz, String sql, PageBean pageBean, List<Object> args)
            throws Exception {
        return executeQryListByPage(clazz, sql, pageBean, list2Arr(args));
    }

    /**
     * 
     * @param clazz
     * @param sql
     * @param pageBean
     * @param args
     * @return
     * @throws Exception
     */
    protected <T> List<T> executeQryListByPage(Class<T> clazz, String sql, PageBean pageBean, Object... args)
            throws Exception {
        // sqlserver ORDER BY 在子查詢中無效，所以刪除order by語句，用pageBean中值來做排序等。
        if (StringUtils.containsIgnoreCase(sql, "order by")) {
            sql = sql.replaceAll(" order by(.*)$", " ");
        }

        // 先設置總記錄數，不管是否分頁都需要這個資訊
        if (pageBean != null) {
            pageBean.setTotalRecords(countQuery(sql, args));
        }

        // 是否分頁查詢
        if (pageBean == null || !pageBean.isPageable() || pageBean.getSortColumn() == null)
            return executeQryList(clazz, sql, args);

        int rowFrom = pageBean.getFirstIndexInPage() - 1;

        // String pageSql = "SELECT row_number() OVER (ORDER BY "+
        // pageBean.getSortColumn() + " " + pageBean.getSortDirection() +") AS
        // ROW_NO,t.* FROM (SELECT row_number() OVER (ORDER BY "
        // + pageBean.getSortColumn() + " " + pageBean.getSortDirection() +") AS
        // ROWNUM1, d.* from (" + sql
        // + ") d) t where t.ROWNUM1 between " + rowFrom + " and " + rowTo;

        String pageSql = "SELECT t.* FROM (SELECT d.* from (" + sql
                + " ORDER BY " + pageBean.getSortColumn() + " " + pageBean.getSortDirection() +
                ") d) t limit " + rowFrom + "," + pageBean.getPageSize();

        return executeQryList(clazz, pageSql, args);
    }

    /**
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    protected int executeUpdate(String sql, List<Object> args) throws Exception {
        return executeUpdate(sql, list2Arr(args));
    }

    /**
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    protected int executeUpdate(String sql, Object... args) throws Exception {
        return jdbcTemplate.update(sql, args);
    }

    /**
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    protected int[] executeBatch(String sql, List<Object[]> batchArgs) throws Exception {
        if (sql == null || sql.isEmpty())
            return new int[0];

        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    /**
     * 
     * @param procedureName
     * @param inParamMap
     * @return
     * @throws Exception
     */
    protected Map<String, Object> executeProcedure(String procedureName, Map<String, Object> inParams)
            throws Exception {
        return executeProcedure(null, procedureName, inParams);
    }

    /**
     * 
     * @param packageName
     * @param procedureName
     * @param inParamMap
     * @return
     * @throws Exception
     */
    protected Map<String, Object> executeProcedure(String packageName, String procedureName,
            Map<String, Object> inParams) throws Exception {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(procedureName);
        if (packageName != null) {
            jdbcCall.withCatalogName(packageName);
        }

        SqlParameterSource is = new MapSqlParameterSource(inParams);
        return jdbcCall.execute(is);
    }

    /**
     * 
     * @param args
     * @return
     */
    private Object[] list2Arr(List<Object> args) {
        return args == null ? null : args.toArray();
    }
}

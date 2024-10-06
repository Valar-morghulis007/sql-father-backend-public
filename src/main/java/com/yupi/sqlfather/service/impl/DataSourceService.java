package com.yupi.sqlfather.service.impl;

import com.yupi.sqlfather.common.BaseResponse;
import com.yupi.sqlfather.common.ErrorCode;
import com.yupi.sqlfather.common.ResultUtils;
import com.yupi.sqlfather.exception.BusinessException;
import com.yupi.sqlfather.mapper.DBInfoMapper;
import com.yupi.sqlfather.model.dto.DBInfoRequest;
import com.yupi.sqlfather.model.entity.DBInfo;
import com.yupi.sqlfather.model.vo.FieldVO;
import com.yupi.sqlfather.utils.DataSourceUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author morghulis
 * @Title: DataSourceService
 * @Description:
 * @date 2024/9/13 20:36
 */
@Service
public class DataSourceService {


    @Autowired
    private DBInfoMapper dbInfoMapper;

    public int save(DBInfoRequest dbInfoRequest) {
        if (dbInfoRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DBInfo dbInfo = new DBInfo();
        dbInfo.setIsDelete(0);
        dbInfo.setCreateTime(new Date());
        BeanUtils.copyProperties(dbInfoRequest, dbInfo);
        Integer result = dbInfoMapper.insert(dbInfo);
        if (result == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return result;
    }

    public boolean testConnect(String id) throws SQLException {
        DBInfo dbInfo = getDbInfo(id);
        String url = DataSourceUtil.getURL(dbInfo.getIp(), dbInfo.getPort(), dbInfo.getDbName());
        try (Connection conn = DriverManager.getConnection(url, dbInfo.getUsername(), dbInfo.getPassword());
             Statement stmt = conn.createStatement()) {
            String tablesSQL = String.format("SELECT VERSION() AS version;");
            ResultSet resultSet = stmt.executeQuery(tablesSQL);
            if (resultSet != null) {
                return true;
            }
        } catch (Exception e) {
            throw new BusinessException(50000, "测试连接失败");
        }
        return false;
    }

    public List getTables(String id) throws SQLException {
        DBInfo dbInfo = getDbInfo(id);
        List<String> tables = new ArrayList<>();
        String url = DataSourceUtil.getURL(dbInfo.getIp(), dbInfo.getPort(), dbInfo.getDbName());
        try (Connection conn = DriverManager.getConnection(url, dbInfo.getUsername(), dbInfo.getPassword());
             Statement stmt = conn.createStatement()) {
            String tablesSQL = String.format("SELECT table_name FROM information_schema.tables WHERE table_schema ='%s'", dbInfo.getDbName());
            ResultSet resultSet = stmt.executeQuery(tablesSQL);
            while (resultSet.next()) {
                tables.add(resultSet.getString("table_name"));
            }
        } catch (Exception e) {
            throw new BusinessException(40000, "获取表列表失败");
        }
        return tables;
    }

    public List getColumns(String id, String dbName, String tableName) throws SQLException {
        DBInfo dbInfo = getDbInfo(id);
        List<FieldVO> fieldVOs = new ArrayList<>();
        String url = DataSourceUtil.getURL(dbInfo.getIp(), dbInfo.getPort(), dbInfo.getDbName());
        try (Connection conn = DriverManager.getConnection(url, dbInfo.getUsername(), dbInfo.getPassword());
             Statement stmt = conn.createStatement()) {
            String columnsSQL = String.format(
                    "SELECT COLUMN_NAME,COLUMN_TYPE,COLUMN_DEFAULT,IS_NULLABLE,COLUMN_COMMENT,COLUMN_KEY,EXTRA " +
                            "FROM INFORMATION_SCHEMA.COLUMNS " +
                            "WHERE TABLE_SCHEMA = '%s' AND TABLE_NAME = '%s';", dbName, tableName);
            ResultSet resultSet = stmt.executeQuery(columnsSQL);
            while (resultSet.next()) {
                FieldVO fieldVO = new FieldVO();
                fieldVO.setFieldName(resultSet.getString("COLUMN_NAME"));
                fieldVO.setFieldType(resultSet.getString("COLUMN_TYPE"));
                fieldVO.setDefaultValue(resultSet.getString("COLUMN_DEFAULT"));
                boolean notNull = resultSet.getString("IS_NULLABLE").equals("YES") ? false : true;
                fieldVO.setNotNull(notNull);
                fieldVO.setComment(resultSet.getString("COLUMN_COMMENT"));
                boolean primaryKey = resultSet.getString("COLUMN_KEY").equals("PRI") ? true : false;
                fieldVO.setPrimaryKey(primaryKey);
                fieldVO.setOnUpdate(resultSet.getString("EXTRA"));
                fieldVOs.add(fieldVO);
            }
        } catch (Exception e) {
            throw new BusinessException(40000, "获取表字段信息失败");
        }
        return fieldVOs;
    }

    private DBInfo getDbInfo(String id) {
        DBInfo dbInfo = dbInfoMapper.selectById(id);
        if (dbInfo == null) {
            throw new BusinessException(00003, "数据库不存在");
        }
        return dbInfo;
    }
}

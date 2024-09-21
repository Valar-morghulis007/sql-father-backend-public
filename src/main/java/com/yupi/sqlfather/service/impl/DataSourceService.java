package com.yupi.sqlfather.service.impl;

import com.yupi.sqlfather.common.BaseResponse;
import com.yupi.sqlfather.common.ErrorCode;
import com.yupi.sqlfather.common.ResultUtils;
import com.yupi.sqlfather.exception.BusinessException;
import com.yupi.sqlfather.mapper.DBInfoMapper;
import com.yupi.sqlfather.model.dto.DBInfoRequest;
import com.yupi.sqlfather.model.entity.DBInfo;
import com.yupi.sqlfather.model.vo.FieldVO;
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

    public BaseResponse<Integer> save(DBInfoRequest dbInfoRequest) {
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
        return ResultUtils.success(result);
    }

    public BaseResponse<Boolean> testConnect(String id) throws SQLException {
        Connection connection = getConnection(id);
        if (connection != null) {
            return ResultUtils.success(true);
        } else {
            return ResultUtils.error(00001, "数据库连接失败");
        }
    }

    public BaseResponse<List> getTables(String id) throws SQLException {
        DBInfo dbInfo = getDbInfo(id);
        Connection connection = getConnection(id);
        String tablesSQL = String.format("SELECT table_name FROM information_schema.tables WHERE table_schema ='%s'", dbInfo.getDbName());
        PreparedStatement preparedStatement = connection.prepareStatement(tablesSQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> tables = new ArrayList<>();
        while (resultSet.next()) {
            tables.add(resultSet.getString(1));
        }
        return ResultUtils.success(tables);
    }

    public BaseResponse<List> getColumns(String id, String dbName, String tableName) throws SQLException {
        Connection connection = getConnection(id);
        String columnsSQL = String.format(
                "SELECT COLUMN_NAME,COLUMN_TYPE,COLUMN_DEFAULT,IS_NULLABLE,COLUMN_COMMENT,COLUMN_KEY,EXTRA " +
                        "FROM INFORMATION_SCHEMA.COLUMNS " +
                        "WHERE TABLE_SCHEMA = '%s' AND TABLE_NAME = '%s';", dbName, tableName);
        PreparedStatement preparedStatement = connection.prepareStatement(columnsSQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<FieldVO> fieldVOS = new ArrayList<>();
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
            fieldVOS.add(fieldVO);
        }
        return ResultUtils.success(fieldVOS);
    }

    private DBInfo getDbInfo(String id) {
        DBInfo dbInfo = dbInfoMapper.selectById(id);
        if (dbInfo == null) {
            throw new BusinessException(00003, "数据库不存在");
        }
        return dbInfo;
    }

    private Connection getConnection(String id) throws SQLException {
        DBInfo dbInfo = dbInfoMapper.selectById(id);
        String url = String.format("jdbc:mysql://%s:%s/%s", dbInfo.getIp(), dbInfo.getPort(), dbInfo.getDbName());
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url(url)
                .username(dbInfo.getUsername())
                .password(dbInfo.getPassword())
                .build();
        Connection connection = dataSource.getConnection();
        if (connection == null) {
            throw new BusinessException(00002, "数据库连接失败");
        }
        return connection;
    }


}

package com.yupi.sqlfather.service.impl;

import com.yupi.sqlfather.common.BaseResponse;
import com.yupi.sqlfather.common.ErrorCode;
import com.yupi.sqlfather.common.ResultUtils;
import com.yupi.sqlfather.config.DataSourceConfig;
import com.yupi.sqlfather.exception.BusinessException;
import com.yupi.sqlfather.mapper.DBInfoMapper;
import com.yupi.sqlfather.model.dto.DBInfoRequest;
import com.yupi.sqlfather.model.entity.DBInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

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

    public void tryConnect(String ip, String port, String database, String username, String password) throws SQLException {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        Connection connection = dataSourceConfig.getDataSource(ip, port, database, username, password).getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE product_brand_info SET modified_time=NOW() WHERE brand_id=1");
        boolean execute = preparedStatement.execute();
        System.out.println(execute);
    }

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
}

package com.yupi.sqlfather.utils;

import cn.hutool.db.Db;
import com.yupi.sqlfather.exception.BusinessException;
import com.yupi.sqlfather.mapper.DBInfoMapper;
import com.yupi.sqlfather.model.entity.DBInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author morghulis
 * @Title: DataSourceUtil
 * @Description:
 * @date 2024/9/22 17:29
 */
@Component
public class DataSourceUtil {
    @Resource
    private DBInfoMapper dbInfoMapper;

    public DBInfo getDbInfo(String id) {
        DBInfo dbInfo = dbInfoMapper.selectById(id);
        if (dbInfo == null) {
            throw new BusinessException(00003, "数据库不存在");
        }
        return dbInfo;
    }

    public Connection getConnection(String id) throws SQLException {
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

    public static String getURL(String ip, String port, String database) {
        String url = String.format("jdbc:mysql://%s:%s/%s?allowLoadLocalInfile=true", ip, port, database);
        return url;
    }
}

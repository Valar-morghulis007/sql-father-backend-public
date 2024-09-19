package com.yupi.sqlfather.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author morghulis
 * @Title: DataSourceConfig
 * @Description:
 * @date 2024/9/13 20:15
 */
public class DataSourceConfig extends BasicDataSource {
    public DataSource getDataSource(String ip,String port,String database,String username,String password)
            throws SQLException {
        super.setDriverClassName("com.mysql.cj.jdbc.Driver");
        String url="jdbc:mysql"+"://"+ip+":"+port+"/"+database;
        super.setUrl(url);
        super.setUsername(username);
        super.setPassword(password);
        return super.createDataSource();
    }
}

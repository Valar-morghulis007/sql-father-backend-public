package com.yupi.sqlfather.model.dto;

import lombok.Data;

/**
 * @author morghulis
 * @Title: DataSourceDto
 * @Description:
 * @date 2024/9/13 20:49
 */
@Data
public class DataSourceDto {

    private String ip;
    private String port;
    private String database;
    private String username;
    private String password;
}

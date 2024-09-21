package com.yupi.sqlfather.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author morghulis
 * @Title: DBInfo
 * @Description:
 * @date 2024/9/19 22:52
 */
@Data
public class DBInfoRequest {
    private String name;
    private String dbName;
    private String dbType;
    private String ip;
    private String port;
    private String username;
    private String password;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}

package com.yupi.sqlfather.model.entity;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author morghulis
 * @Title: DBInfo
 * @Description:
 * @date 2024/9/19 22:52
 */
@TableName(value = "db_info")
@Data
public class DBInfo {
    /**
     * 表ID，自增主键
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 数据库别名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 数据库名称
     */
    @TableField(value = "db_name")
    private String dbName;

    /**
     * 数据库类型
     */
    @TableField(value = "db_type")
    private String dbType;

    /**
     * 数据库IP
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 数据库端口
     */
    @TableField(value = "port")
    private String port;
    /**
     * 数据库用户
     */
    @TableField(value = "username")
    private String username;

    /**
     * 数据库密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;
}

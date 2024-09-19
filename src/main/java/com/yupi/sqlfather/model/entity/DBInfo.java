package com.yupi.sqlfather.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author morghulis
 * @Title: DBInfo
 * @Description:
 * @date 2024/9/19 22:52
 */
@TableName(value ="db_info")
@Data
public class DBInfo {
    @TableField("id")
    private Long id;
    @TableField("name")
    private String name;
    @TableField("db_name")
    private String dbName;
    @TableField("db_type")
    private String dbType;
    @TableField("ip")
    private String ip;
    @TableField("port")
    private String port;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField("is_delete")
    private Integer isDelete;
}

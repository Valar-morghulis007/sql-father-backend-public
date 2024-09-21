package com.yupi.sqlfather.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author morghulis
 * @Title: DbVO
 * @Description:
 * @date 2024/9/21 9:56
 */
@Data
public class FieldVO implements Serializable {
    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 是否非空
     */
    private boolean notNull;

    /**
     * 注释（字段中文名）
     */
    private String comment;

    /**
     * 是否为主键
     */
    private boolean primaryKey;

    /**
     * 是否自增
     */
    private boolean autoIncrement;

    /**
     * 模拟类型（随机、图片、规则、词库）
     */
    private String mockType;

    /**
     * 模拟参数
     */
    private String mockParams;

    /**
     * 附加条件
     */
    private String onUpdate;
}

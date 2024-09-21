package com.yupi.sqlfather.service.impl;

import com.yupi.sqlfather.common.BaseResponse;
import com.yupi.sqlfather.common.ResultUtils;
import com.yupi.sqlfather.core.builder.DataBuilder;
import com.yupi.sqlfather.core.schema.TableSchema;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author morghulis
 * @Title: DataSourceService
 * @Description:
 * @date 2024/9/13 20:36
 */
@Service
public class GenerateDataService {

    public BaseResponse<List> generateData(TableSchema tableSchema) {
        int mockNum = tableSchema.getMockNum();
        // 生成模拟数据
        List<Map<String, Object>> dataList = DataBuilder.generateData(tableSchema, mockNum);
        return ResultUtils.success(dataList);
    }
}

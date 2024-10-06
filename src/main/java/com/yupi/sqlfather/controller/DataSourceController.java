package com.yupi.sqlfather.controller;

import com.yupi.sqlfather.common.BaseResponse;
import com.yupi.sqlfather.common.ResultUtils;
import com.yupi.sqlfather.model.dto.DBInfoRequest;
import com.yupi.sqlfather.service.impl.DataSourceService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * @author morghulis
 * @Title: DataSourceController
 * @Description:
 * @date 2024/9/13 20:47
 */
@RestController
@RequestMapping("/data_source")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @GetMapping("/connect")
    public BaseResponse<Boolean> testConnect(@Param("id") String id)
            throws SQLException {
        boolean b = dataSourceService.testConnect(id);
        if (!b) {
            return ResultUtils.error(60000, "数据库连接失败");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/add")
    public BaseResponse<Integer> addDB(@RequestBody DBInfoRequest dbInfoRequest) {
        int save = dataSourceService.save(dbInfoRequest);
        if (save != 1) {
            return ResultUtils.error(30000, "新增数据库失败");
        }
        return ResultUtils.success(save);
    }

    @GetMapping("/get/tables")
    public BaseResponse<List> getTables(@RequestParam("id") String id) throws SQLException {
        List tables = dataSourceService.getTables(id);
        if (CollectionUtils.isEmpty(tables)) {
            return ResultUtils.error(50000, "获取表列表失败或没有表");
        }
        return ResultUtils.success(tables);
    }

    @GetMapping("/get/columns")
    public BaseResponse<List> getColumns(@RequestParam("id") String id,
                                         @RequestParam("dbName") String dbName,
                                         @RequestParam("tableName") String tableName)
            throws SQLException {
        List columns = dataSourceService.getColumns(id, dbName, tableName);
        if(CollectionUtils.isEmpty(columns)){
            return ResultUtils.error(50000, "获取字段列表失败");
        }
        return ResultUtils.success(columns);
    }


}

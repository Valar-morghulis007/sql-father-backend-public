package com.yupi.sqlfather.controller;

import com.yupi.sqlfather.common.BaseResponse;
import com.yupi.sqlfather.model.dto.DBInfoRequest;
import com.yupi.sqlfather.model.dto.DataSourceDto;
import com.yupi.sqlfather.service.impl.DataSourceService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
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
        return dataSourceService.testConnect(id);
    }

    @PostMapping("/add")
    public BaseResponse<Integer> addDB(@RequestBody DBInfoRequest dbInfoRequest) {
        return dataSourceService.save(dbInfoRequest);
    }

    @GetMapping("/get/tables")
    public BaseResponse<List> getTables(@RequestParam("id") String id) throws SQLException {
        return dataSourceService.getTables(id);
    }

    @GetMapping("/get/columns")
    public BaseResponse<List> getColumns(@RequestParam("id") String id,
                                         @RequestParam("dbName") String dbName,
                                         @RequestParam("tableName") String tableName)
            throws SQLException {
        return dataSourceService.getColumns(id, dbName, tableName);
    }


}

package com.yupi.sqlfather.controller;

import com.yupi.sqlfather.common.BaseResponse;
import com.yupi.sqlfather.common.ResultUtils;
import com.yupi.sqlfather.core.schema.TableSchema;
import com.yupi.sqlfather.service.impl.DataSourceService;
import com.yupi.sqlfather.service.impl.GenerateDataService;
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
@RequestMapping("/generate_data")
public class GenerateDataController {

    @Autowired
    private GenerateDataService generateDataService;


    @PostMapping("/mock_list")
    public BaseResponse<List> generateData(@RequestBody TableSchema tableSchema) {
        List result = generateDataService.generateData(tableSchema);
        if (result != null) {
            return ResultUtils.success(result);
        }else {
            return ResultUtils.error(3000, "数据生成失败");
        }
    }

    @PostMapping("/generate_file")
    public BaseResponse<Boolean> generateFile(@RequestBody TableSchema tableSchema) {
        boolean result = generateDataService.generateFile(tableSchema);
        if (result) {
            return ResultUtils.success(true);
        } else {
            return ResultUtils.error(3000, "文件生成失败");
        }
    }

    @GetMapping("/import_db")
    public BaseResponse<Boolean> importDb(@RequestParam("id") String id,
                                          @RequestParam("dbName") String dbName,
                                          @RequestParam("tableName") String tableName) throws SQLException {

        boolean result = generateDataService.importDb(id, dbName, tableName);
        if (result) {
            return ResultUtils.success(true);
        } else {
            return ResultUtils.error(2000, "数据导入失败");
        }

    }


}

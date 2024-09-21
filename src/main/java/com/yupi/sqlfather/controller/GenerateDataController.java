package com.yupi.sqlfather.controller;

import com.yupi.sqlfather.common.BaseResponse;
import com.yupi.sqlfather.core.schema.TableSchema;
import com.yupi.sqlfather.service.impl.DataSourceService;
import com.yupi.sqlfather.service.impl.GenerateDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return generateDataService.generateData(tableSchema);
    }


}

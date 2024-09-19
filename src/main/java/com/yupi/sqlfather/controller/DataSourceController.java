package com.yupi.sqlfather.controller;

import com.yupi.sqlfather.common.BaseResponse;
import com.yupi.sqlfather.model.dto.DBInfoRequest;
import com.yupi.sqlfather.model.dto.DataSourceDto;
import com.yupi.sqlfather.service.impl.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

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

    @PostMapping("/connect")
    public String tryConnect(@RequestBody DataSourceDto dataSourceDto) throws SQLException {
        dataSourceService.tryConnect(dataSourceDto.getIp(),dataSourceDto.getPort(),dataSourceDto.getDatabase(),
                dataSourceDto.getUsername(),dataSourceDto.getPassword());
        return "success";
    }

    @PostMapping("/add")
    public BaseResponse<Integer> addDB(@RequestBody DBInfoRequest dbInfoRequest) {
        return dataSourceService.save(dbInfoRequest);
    }


}

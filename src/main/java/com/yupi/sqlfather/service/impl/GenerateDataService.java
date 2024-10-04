package com.yupi.sqlfather.service.impl;

import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.databind.deser.std.MapEntryDeserializer;
import com.yupi.sqlfather.common.BaseResponse;
import com.yupi.sqlfather.common.ResultUtils;
import com.yupi.sqlfather.constant.CommonConstant;
import com.yupi.sqlfather.core.builder.DataBuilder;
import com.yupi.sqlfather.core.schema.TableSchema;
import com.yupi.sqlfather.model.entity.DBInfo;
import com.yupi.sqlfather.model.vo.FieldVO;
import com.yupi.sqlfather.utils.DataSourceUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author morghulis
 * @Title: DataSourceService
 * @Description:
 * @date 2024/9/13 20:36
 */
@Service
public class GenerateDataService {

    @Value("${file.path.generate}")
    private String filePathPrefix;

    @Value("${data.mock.num}")
    private Integer mockNum;

    @Autowired
    private DataSourceUtil dataSourceUtil;


    public List<Map<String, Object>> generateData(TableSchema tableSchema) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        long mockNumTotal = tableSchema.getMockNum();
        if (mockNumTotal < mockNum) {
            mockNum = (int) mockNumTotal;
        }
        while (mockNumTotal > 0) {
            List<Map<String, Object>> dataList = DataBuilder.generateData(tableSchema, mockNum);
            resultList.addAll(dataList);
            mockNumTotal -= mockNum;
            if (mockNumTotal < 0) {
                mockNum = (int) mockNumTotal;
            }
        }
        return resultList;
    }

    public boolean generateFile(TableSchema tableSchema) {
        long mockNumTotal = tableSchema.getMockNum();
        String filePath = filePathPrefix + tableSchema.getTableName();
        if (!new File(filePath).exists()) {
            FileUtil.mkdir(new File(filePath));
        }
        if (mockNumTotal < mockNum) {
            mockNum = (int) mockNumTotal;
        }
        int count = 1;
        while (mockNumTotal > 0) {
            String filePathAbsolute = filePath + File.separator + tableSchema.getTableName() + "_" + count
                    + CommonConstant.FILE_NAME_SUFFIX_BIN;
            // 生成模拟数据
            List<Map<String, Object>> dataList = DataBuilder.generateData(tableSchema, mockNum);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePathAbsolute), "UTF8"))) {
                for (Map<String, Object> map : dataList) {
                    String line = map.values().stream().map(element -> String.valueOf(element))
                            .collect(Collectors.joining("\u001B"));
                    writer.write(line + "\r\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mockNumTotal -= mockNum;
            if (mockNumTotal < 0) {
                mockNum = (int) mockNumTotal;
            }
            count++;
        }
        return true;
    }

    public boolean importDb(String id, String dbName, String tableName) {
        String filePath = filePathPrefix + File.separator + tableName;
        File folderFile = new File(filePath);
        File[] files = folderFile.listFiles();
        boolean result = false;
        DBInfo dbInfo = dataSourceUtil.getDbInfo(id);
        String url = DataSourceUtil.getURL(dbInfo.getIp(), dbInfo.getPort(), dbInfo.getDbName());
        try (Connection conn = DriverManager.getConnection(url, dbInfo.getUsername(), dbInfo.getPassword());
             Statement stmt = conn.createStatement()) {
            String columnsNameSQL = String.format(
                    "SELECT COLUMN_NAME,COLUMN_KEY,EXTRA FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '%s' AND TABLE_NAME = '%s';",
                    dbName, tableName);
            ResultSet resultSet = stmt.executeQuery(columnsNameSQL);
            List<String> fieldNames = new ArrayList<>();
            while (resultSet.next()) {
                String columnKey = resultSet.getString("COLUMN_KEY");
                String extra = resultSet.getString("EXTRA");
                if (!"PRI".equals(columnKey) && !"auto_increment".equals(extra)) {
                    fieldNames.add(resultSet.getString("COLUMN_NAME"));
                }
            }
            String fields = fieldNames.stream().collect(Collectors.joining(","));
            for (File file : files) {
                String path = Paths.get(filePathPrefix, tableName, file.getName()).toString();
                path = path.replace("\\", "\\\\");
                StringBuffer sbSql = new StringBuffer();
                sbSql.append("LOAD DATA LOCAL INFILE '")
                        .append(path)
                        .append("' INTO TABLE ")
                        .append(tableName)
                        .append(" FIELDS TERMINATED BY '\u001B' ")
                        .append("OPTIONALLY ENCLOSED BY '\"' ")
                        .append("LINES TERMINATED BY '\\r\\n' ")
                        .append("IGNORE 0 LINES ")
                        .append("(")
                        .append(fields)
                        .append(")");
                String sbSQL = sbSql.toString();
                result = stmt.execute(sbSQL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !result;
    }
}

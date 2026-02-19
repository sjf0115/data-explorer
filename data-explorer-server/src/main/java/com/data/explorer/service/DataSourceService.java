package com.data.explorer.service;

import com.data.explorer.entity.*;
import com.data.explorer.mapper.*;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSourceService {
    private final Gson gson = new Gson().newBuilder().create();
    private final DataSourceTypeMapper typeMapper;
    private final DataSourceMapper sourceMapper;
    private final DatabaseInfoMapper databaseMapper;
    private final TableInfoMapper tableMapper;
    
    public List<DataSourceType> getAllTypes() {
        List<DataSourceType> dataSourceTypes = typeMapper.findAll();
        log.info("获取所有数据源类型: {}", gson.toJson(dataSourceTypes));
        return dataSourceTypes;
    }
    
    public List<DataSource> getSourcesByType(String typeCode) {
        return sourceMapper.findByTypeCode(typeCode);
    }
    
    public List<DatabaseInfo> getDatabasesBySource(Long dataSourceId) {
        return databaseMapper.findByDataSourceId(dataSourceId);
    }
    
    public List<TableInfo> getTablesByDatabase(Long databaseId) {
        return tableMapper.findByDatabaseId(databaseId);
    }
}

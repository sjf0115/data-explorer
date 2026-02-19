package com.data.explorer.controller;

import com.data.explorer.common.Result;
import com.data.explorer.entity.*;
import com.data.explorer.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/datasource")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DataSourceController {
    @Autowired
    private final DataSourceService dataSourceService;
    
    @GetMapping("/types")
    public Result<List<DataSourceType>> getTypes() {
        return Result.success(dataSourceService.getAllTypes());
    }
    
    @GetMapping("/list")
    public Result<List<DataSource>> getSources(@RequestParam String typeCode) {
        return Result.success(dataSourceService.getSourcesByType(typeCode));
    }
    
    @GetMapping("/{dataSourceId}/databases")
    public Result<List<DatabaseInfo>> getDatabases(@PathVariable Long dataSourceId) {
        return Result.success(dataSourceService.getDatabasesBySource(dataSourceId));
    }
    
    @GetMapping("/database/{databaseId}/tables")
    public Result<List<TableInfo>> getTables(@PathVariable Long databaseId) {
        return Result.success(dataSourceService.getTablesByDatabase(databaseId));
    }
}

package com.data.explorer.mapper;

import com.data.explorer.entity.ExplorerTask;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ExplorerTaskMapper {
    
    @Insert("INSERT INTO explorer_task (task_name, data_source_id, database_id, table_id, " +
            "field_explorer, sample_rate, parallelism, status, message) " +
            "VALUES (#{taskName}, #{dataSourceId}, #{databaseId}, #{tableId}, " +
            "#{fieldExplorer}, #{sampleRate}, #{parallelism}, #{status}, #{message})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ExplorerTask task);
    
    @Select("SELECT * FROM explorer_task ORDER BY created_at DESC")
    List<ExplorerTask> findAll();
}

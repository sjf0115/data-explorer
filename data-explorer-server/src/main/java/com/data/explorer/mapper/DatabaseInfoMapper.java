package com.data.explorer.mapper;

import com.data.explorer.entity.DatabaseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface DatabaseInfoMapper {
    
    @Select("SELECT * FROM database_info WHERE data_source_id = #{dataSourceId}")
    List<DatabaseInfo> findByDataSourceId(@Param("dataSourceId") Long dataSourceId);
}

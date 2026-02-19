package com.data.explorer.mapper;

import com.data.explorer.entity.TableInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface TableInfoMapper {
    
    @Select("SELECT * FROM table_info WHERE database_id = #{databaseId}")
    List<TableInfo> findByDatabaseId(@Param("databaseId") Long databaseId);
}

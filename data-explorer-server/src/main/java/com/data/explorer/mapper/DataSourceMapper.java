package com.data.explorer.mapper;

import com.data.explorer.entity.DataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface DataSourceMapper {
    
    @Select("SELECT * FROM data_source WHERE type_code = #{typeCode} AND status = 1")
    List<DataSource> findByTypeCode(@Param("typeCode") String typeCode);
}

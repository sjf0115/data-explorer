package com.data.explorer.mapper;

import com.data.explorer.entity.DataSourceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface DataSourceTypeMapper {
    
    @Select("SELECT * FROM data_source_type ORDER BY sort_order")
    List<DataSourceType> findAll();
}

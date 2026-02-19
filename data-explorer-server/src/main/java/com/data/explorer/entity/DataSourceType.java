package com.data.explorer.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DataSourceType {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String icon;
    private Integer sortOrder;
    private String createdAt;
    private String updatedAt;
}

package com.data.explorer.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TableInfo {
    private Long id;
    private Long databaseId;
    private String name;
    private String description;
    private Long rowCount;
    private LocalDateTime createdAt;
}

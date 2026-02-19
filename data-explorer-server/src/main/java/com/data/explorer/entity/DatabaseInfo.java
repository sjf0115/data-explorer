package com.data.explorer.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DatabaseInfo {
    private Long id;
    private Long dataSourceId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}

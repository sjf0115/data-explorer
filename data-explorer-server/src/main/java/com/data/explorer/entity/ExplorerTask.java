package com.data.explorer.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExplorerTask {
    private Long id;
    private String taskName;
    private Long dataSourceId;
    private Long databaseId;
    private Long tableId;
    private Boolean fieldExplorer;
    private Integer sampleRate;
    private Integer parallelism;
    private String status;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

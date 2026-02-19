package com.data.explorer.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DataSource {
    private Long id;
    private String name;
    private String typeCode;
    private String host;
    private Integer port;
    private String databaseName;
    private String username;
    private String password;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

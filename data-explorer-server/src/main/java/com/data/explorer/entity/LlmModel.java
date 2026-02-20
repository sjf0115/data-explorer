package com.data.explorer.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 大模型配置实体
 */
@Data
public class LlmModel {
    private Long id;
    private String name;
    private String modelId;
    private String provider;
    private String baseUrl;
    private String apiKey;
    private String description;
    private Boolean isDefault;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

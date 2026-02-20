package com.data.explorer.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 对话会话实体
 */
@Data
public class ChatConversation {
    private Long id;
    private String conversationId;
    private String title;
    private String userId;
    private Long modelId;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 关联模型信息
    private LlmModel model;
}

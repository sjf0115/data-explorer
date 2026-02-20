package com.data.explorer.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 消息记录实体
 */
@Data
public class ChatMessage {
    private Long id;
    private String conversationId;
    private String messageId;
    private String role;
    private String content;
    private String parentMessageId;
    private Integer tokensUsed;
    private Integer status;
    private LocalDateTime createdAt;
}

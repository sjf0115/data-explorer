package com.data.explorer.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private Integer status;
    private String userId;
    private String userName;
    private String password;
    private String creator;
    private String modifier;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}

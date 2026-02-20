-- 1. 用户
DROP Table `tb_user`;
CREATE TABLE IF NOT EXISTS `tb_user`(
    `id` BIGINT UNSIGNED AUTO_INCREMENT COMMENT '自增ID',
    `status` INT NOT NULL DEFAULT 1 COMMENT '状态:1-启用,2-停用',
    `user_id` VARCHAR(40) NOT NULL COMMENT '用户ID',
    `user_name` VARCHAR(100) NOT NULL COMMENT '用户名称',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `creator` VARCHAR(100) NOT NULL COMMENT '创建者',
    `modifier` VARCHAR(100) NOT NULL COMMENT '修改者',
    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户';

INSERT INTO tb_user (user_id, user_name, password, creator, modifier)
VALUES ('admin', '管理员', MD5('123456'), 'system', 'system');

-- 2. 数据源类型表
CREATE TABLE IF NOT EXISTS data_source_type (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    icon VARCHAR(50),
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='数据源类型';

-- 3. 数据源表
CREATE TABLE IF NOT EXISTS data_source (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type_code VARCHAR(50) NOT NULL,
    host VARCHAR(255),
    port INT,
    database_name VARCHAR(100),
    username VARCHAR(100),
    password VARCHAR(255),
    status INT DEFAULT 1 COMMENT '1:启用 0:禁用',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
);

drop table data_source;
INSERT INTO data_source (name, type_code, host, port, status) VALUES
('生产环境Hive', 'hive', 'emr-serverless.aliyuncs.com', 443, 1),
('业务MySQL', 'mysql', 'mysql.example.com', 3306, 1),
('分析PostgreSQL', 'postgresql', 'pg.example.com', 5432, 1);

-- 4. 数据库表
CREATE TABLE IF NOT EXISTS database_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    data_source_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 5. 数据表信息
CREATE TABLE IF NOT EXISTS table_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    database_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    row_count BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. 探查任务表
CREATE TABLE IF NOT EXISTS explorer_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_name VARCHAR(200) NOT NULL,
    data_source_id BIGINT NOT NULL,
    database_id BIGINT NOT NULL,
    table_id BIGINT NOT NULL,
    field_explorer BOOLEAN DEFAULT TRUE,
    sample_rate INT DEFAULT 100,
    parallelism INT DEFAULT 4,
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING/RUNNING/SUCCESS/FAILED',
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 初始化数据
INSERT INTO data_source_type (code, name, description, icon, sort_order) VALUES
('hive', 'Hive', 'Hive', 'database', 1),
('mysql', 'MySQL', 'MySQL', 'mysql', 2),
('postgresql', 'PostgreSQL', 'PostgreSQL数据库', 'postgresql', 3);



INSERT INTO database_info (data_source_id, name, description) VALUES
(1, 'default', '默认数据库'),
(1, 'ods', 'ODS层数据'),
(1, 'dwd', 'DWD层数据'),
(2, 'business_db', '业务数据库'),
(2, 'log_db', '日志数据库'),
(3, 'analytics', '分析数据库');

INSERT INTO table_info (database_id, name, description, row_count) VALUES
(1, 'user_info', '用户信息表', 1000000),
(1, 'order_info', '订单信息表', 5000000),
(1, 'product_info', '商品信息表', 100000),
(2, 'user_behavior', '用户行为表', 10000000),
(3, 'sales_summary', '销售汇总表', 50000),
(4, 'customers', '客户表', 200000),
(5, 'access_logs', '访问日志表', 50000000);

-- ============================================
-- 大模型对话平台表结构
-- ============================================

-- 7. 大模型配置表
CREATE TABLE IF NOT EXISTS llm_model (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '模型名称',
    model_id VARCHAR(100) NOT NULL COMMENT '模型标识(如gpt-3.5-turbo)',
    provider VARCHAR(50) NOT NULL COMMENT '提供商(openai/deepseek等)',
    base_url VARCHAR(255) COMMENT 'API基础URL',
    api_key VARCHAR(255) COMMENT 'API密钥',
    description VARCHAR(255) COMMENT '模型描述',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否默认模型',
    status INT DEFAULT 1 COMMENT '1:启用 0:禁用',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='大模型配置';

-- 8. 对话会话表
CREATE TABLE IF NOT EXISTS chat_conversation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    conversation_id VARCHAR(64) NOT NULL UNIQUE COMMENT '会话唯一标识',
    title VARCHAR(200) COMMENT '会话标题',
    user_id VARCHAR(40) COMMENT '用户ID',
    model_id BIGINT COMMENT '使用的模型ID',
    status INT DEFAULT 1 COMMENT '1:正常 0:已删除',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (model_id) REFERENCES llm_model(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对话会话';

-- 9. 消息记录表
CREATE TABLE IF NOT EXISTS chat_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    conversation_id VARCHAR(64) NOT NULL COMMENT '所属会话ID',
    message_id VARCHAR(64) NOT NULL UNIQUE COMMENT '消息唯一标识',
    role VARCHAR(20) NOT NULL COMMENT '角色: user/assistant/system',
    content TEXT NOT NULL COMMENT '消息内容',
    parent_message_id VARCHAR(64) COMMENT '父消息ID(用于分支对话)',
    tokens_used INT DEFAULT 0 COMMENT '使用的token数',
    status INT DEFAULT 1 COMMENT '1:正常 0:已删除',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (conversation_id) REFERENCES chat_conversation(conversation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息记录';

-- 初始化大模型配置数据
INSERT INTO llm_model (name, model_id, provider, description, is_default, status) VALUES
('DeepSeek Chat', 'deepseek-chat', 'deepseek', 'DeepSeek V3 对话模型', TRUE, 1),
('GPT-3.5 Turbo', 'gpt-3.5-turbo', 'openai', 'OpenAI GPT-3.5 Turbo', FALSE, 1),
('GPT-4', 'gpt-4', 'openai', 'OpenAI GPT-4', FALSE, 1);

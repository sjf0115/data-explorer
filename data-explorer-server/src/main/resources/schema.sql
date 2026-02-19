-- 1. 用户
DROP Table `tb_user`;
CREATE TABLE IF NOT EXISTS `tb_user`(
    `id` BIGINT UNSIGNED AUTO_INCREMENT COMMENT '自增ID',
    `status` INT NOT NULL DEFAULT 1 COMMENT '状态:1-启用,2-停用',
    `user_id` VARCHAR(40) NOT NULL COMMENT '用户ID',
    `user_name` VARCHAR(100) NOT NULL COMMENT '用户名称',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `source_type` INT NOT NULL DEFAULT 1 COMMENT '创建方式: 1-系统内置,2-自定义',
    `creator` VARCHAR(100) NOT NULL COMMENT '创建者',
    `modifier` VARCHAR(100) NOT NULL COMMENT '修改者',
    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户';


--CREATE TABLE `profile_meta_datasource_schema` (
--    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
--    `status` INT NOT NULL DEFAULT 1 COMMENT '状态:1-启用,2-停用',
--    `schema_id` varchar(40) NOT NULL COMMENT '数据源 Schema ID',
--    `schema_name` varchar(100) NOT NULL COMMENT '数据源 Schema 名称',
--    `schema_type` varchar(100) NOT NULL COMMENT '数据源 Schema 类型:1-source,2-sink,3-source/sink',
--    `jdbc_protocol` varchar(50) NOT NULL COMMENT '数据源 Schema JDBC 协议 例如 jdbc://mysql',
--    `source_type` INT NOT NULL DEFAULT 1 COMMENT '创建方式: 1-系统内置,2-自定义',
--    `config_template` text NOT NULL COMMENT '配置模板',
--    `creator` varchar(100) NOT NULL COMMENT '创建者',
--    `modifier` varchar(100) NOT NULL COMMENT '修改者',
--    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
--    `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
--    PRIMARY KEY (`id`),
--    UNIQUE(`schema_id`)
--) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='画像-数据源Schema';

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

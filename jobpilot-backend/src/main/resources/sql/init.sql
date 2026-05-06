create database jobpilot;

use jobpilot;

CREATE TABLE user (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',

                      email VARCHAR(100) UNIQUE COMMENT '邮箱',
                      password VARCHAR(255) COMMENT '密码',
                      username VARCHAR(50) COMMENT '昵称',
                      avatar_url VARCHAR(255) COMMENT '头像URL',

                      role VARCHAR(20) DEFAULT 'user' COMMENT '角色：admin / user',

                      vip_level INT DEFAULT 0 COMMENT 'VIP等级：0免费 1VIP 2企业',
                      vip_expire_time DATETIME COMMENT 'VIP过期时间',

                      status TINYINT DEFAULT 1 COMMENT '状态：1正常 0禁用',

                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE resume (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT,

                        file_name VARCHAR(255),
                        file_url VARCHAR(500),
                        file_type VARCHAR(20),

                        raw_text LONGTEXT,
                        parse_status TINYINT DEFAULT 0,

                        created_at DATETIME,
                        updated_at DATETIME
);

CREATE TABLE resume_detail (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               resume_id BIGINT NOT NULL COMMENT '关联简历ID',

    -- 基本信息
                               name VARCHAR(100),
                               phone VARCHAR(50),
                               email VARCHAR(100),

    -- 核心字段（先用文本存，够用）
                               skills TEXT COMMENT '技能（JSON或文本）',
                               education TEXT COMMENT '教育经历（JSON或文本）',
                               experience TEXT COMMENT '工作经历（JSON或文本）',

    -- ⭐ 核心：用于匹配（非常关键）
                               skill_tags VARCHAR(500) COMMENT '技能标签：Java,SpringBoot,MySQL',

    -- 状态
                               parse_status TINYINT DEFAULT 0 COMMENT '0未解析 1成功 2失败',

                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                               INDEX idx_resume_id (resume_id),
                               INDEX idx_skill_tags (skill_tags)
);
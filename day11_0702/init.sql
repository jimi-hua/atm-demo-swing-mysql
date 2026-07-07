-- ATM系统 — 数据库初始化脚本
-- 使用方法：mysql -u root -p < init.sql

DROP DATABASE IF EXISTS atm;
CREATE DATABASE atm DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE atm;

CREATE TABLE IF NOT EXISTS users (
    id       INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    name     VARCHAR(50)   NOT NULL        COMMENT '用户名',
    id_card  VARCHAR(18)   NOT NULL        COMMENT '身份证号',
    balance  DECIMAL(15,2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
    account  VARCHAR(50)   NOT NULL UNIQUE COMMENT '银行卡号',
    password VARCHAR(50)   NOT NULL        COMMENT '登录密码',
    enable   TINYINT(1)    NOT NULL DEFAULT 1  COMMENT '1-正常 0-冻结'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户账户表';

INSERT INTO users (name, id_card, balance, account, password, enable) VALUES
('xl', '110', 1000.00, '2',    '123456', 1),
('dj', '120', 2000.00, '3422', '1',      1);

SELECT * FROM users;

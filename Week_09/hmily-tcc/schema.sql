CREATE DATABASE IF NOT EXISTS `account_db1` DEFAULT CHARACTER SET utf8mb4;

USE `account_db1`;

DROP TABLE IF EXISTS `t_account`;

CREATE TABLE `t_account` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) NOT NULL COMMENT '用户id',
  `balance_RMB` decimal(10,0) DEFAULT NULL COMMENT '用户人民币余额',
  `balance_USD` decimal(10,0) DEFAULT NULL COMMENT '用户美元余额',
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='A库账户表';

DROP TABLE IF EXISTS `t_freeze`;

CREATE TABLE `t_freeze` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `account_id` int(20) NOT NULL COMMENT '账户id',
  `freeze_amount` decimal(10,0) NOT NULL COMMENT '冻结金额，扣款暂存余额',
  `freeze_type` tinyint(4) NOT NULL COMMENT '货币类型 1->RMB, 2->USD',
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=UTF8MB4_BIN COMMENT='A库冻结表';

INSERT INTO `t_account` VALUES (10, 1, 0.0, 1.0, now(), now());

CREATE DATABASE IF NOT EXISTS `account_db2` DEFAULT CHARACTER SET UTF8MB4;

USE `account_db2`;

DROP TABLE IF EXISTS `t_account`;

CREATE TABLE `t_account` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` INT(20) NOT NULL COMMENT '用户id',
  `balance_RMB` decimal(10,0) DEFAULT NULL COMMENT '用户人民币余额',
  `balance_USD` decimal(10,0) DEFAULT NULL COMMENT '用户美元余额',
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='B库账户表';

DROP TABLE IF EXISTS `t_freeze`;

CREATE TABLE `t_freeze` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `account_id` int(20) NOT NULL COMMENT '账户id',
  `freeze_amount` decimal(10,0) NOT NULL COMMENT '冻结金额，扣款暂存余额',
  `freeze_type` tinyint(4) NOT NULL COMMENT '冻结货币类型 1->RMB, 2->USD',
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='B库冻结表';

INSERT INTO `t_account` VALUES (20, 2, 7.0, 0.0, now(), now());

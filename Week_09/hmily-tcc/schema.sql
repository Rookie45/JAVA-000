CREATE DATABASE IF NOT EXISTS `mall_order` DEFAULT CHARACTER SET utf8mb4;

USE `mall_order`;

DROP TABLE IF EXISTS `t_order_item`;

CREATE TABLE `t_order_item` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `order_id` INT(11) DEFAULT NULL COMMENT '订单id',
  `order_sn` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `order_status` tinyint(4) DEFAULT NULL COMMENT '订单状态：0->待付款;1->支付中;2->支付成功;3->支付失败',
  `product_id` INT(11) DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `product_sn` varchar(64) DEFAULT NULL COMMENT '商品编号',
  `product_quantity` int(11) DEFAULT NULL COMMENT '购买数量',
  `sku_id` INT(11) NULL DEFAULT NULL COMMENT 'sku id',
  `sku_code` VARCHAR(64) NULL DEFAULT NULL COMMENT 'sku 编码',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='订单包含的商品';


CREATE DATABASE IF NOT EXISTS `mall_inventory` DEFAULT CHARACTER SET utf8mb4;

USE `mall_inventory`;

DROP TABLE IF EXISTS `t_sku_inventory`;

CREATE TABLE `t_sku_inventory` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `product_id` INT(11) DEFAULT NULL COMMENT '商品id',
  `sku_code` varchar(64) NOT NULL COMMENT 'sku编码',
  `stock` INT(11) DEFAULT '0' COMMENT '库存',
  `low_stock` INT(11) DEFAULT NULL COMMENT '预警库存',
  `lock_stock` INT(11) DEFAULT '0' COMMENT '锁定库存',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='库存';

INSERT INTO `t_sku_inventory` VALUES (18, 28, '20201208121212', 100, 20, 0, now(), now());

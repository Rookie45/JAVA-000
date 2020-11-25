CREATE TABLE IF NOT EXISTS `tb_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(64) DEFAULT NULL COMMENT '手机号码',
  `icon` varchar(128) DEFAULT NULL COMMENT '头像路径',
  `gender` int(1) DEFAULT NULL COMMENT '性别：0->火星人;1->男;2->女',
  `modify_time` datetime DEFAULT NULL COMMENT '最近修改时间',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idx_username` (`username`),
  UNIQUE KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `tb_order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `order_sn` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `business_id` bigint(20) NOT NULL COMMENT '商家id',
  `product_snapshot` varchar(128) DEFAULT NULL COMMENT '商品快照路径',
  `pay_amount` decimal(10,2) DEFAULT NULL COMMENT '应付金额',
  `order_status` int(1) DEFAULT NULL COMMENT '订单状态：0->待付款;1->已完成;2->已关闭;3->无效订单',
  `note` varchar(255) DEFAULT NULL COMMENT '订单备注',
  `delete_status` int(1) NOT NULL DEFAULT '0' COMMENT '删除状态：0->未删除;1->已删除',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime DEFAULT NULL COMMENT '提交时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';

CREATE TABLE IF NOT EXISTS `tb_business`(
  `business_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商家id',
  `business_name` varchar(64) DEFAULT NULL COMMENT '商家名',
  `description` varchar(255) DEFAULT NULL COMMENT '商家简介',
  `evaluatation` decimal(2,1) COMMENT '好评率: 0.0 -> 5.0',
  `qualification` varchar(128) DEFAULT NULL COMMENT '资质页面路径',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`business_id`),
  UNIQUE KEY `idx_business_name` (`business_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商家表';

CREATE TABLE IF NOT EXISTS `tb_product` (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(64) NOT NULL,
  `product_sn` varchar(64) DEFAULT NULL COMMENT '商品编号',
  `business_id` bigint(20) NOT NULL COMMENT '商家id',
  `product_category_name` varchar(255) DEFAULT NULL COMMENT '商品分类名称',
  `sale` int(11) DEFAULT NULL COMMENT '销量',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `description` varchar(255) COMMENT '商品简述',
  `pic` varchar(128) DEFAULT NULL COMMENT '图片路径',
  `stock` int(11) DEFAULT NULL COMMENT '库存',
  `low_stock` int(11) DEFAULT NULL COMMENT '库存预警值',
  `unit` varchar(16) DEFAULT NULL COMMENT '单位',
  `detail_title` varchar(255) DEFAULT NULL COMMENT '详情标题',
  `detail_desc` varchar(128) COMMENT '详情页面路径',
  `delete_status` int(1) DEFAULT NULL COMMENT '删除状态：0->未删除; 1->已删除',
  `publish_status` int(1) DEFAULT NULL COMMENT '上架状态：0->下架; 1->上架',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime DEFAULT NULL COMMENT '上架时间',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品表';

CREATE TABLE IF NOT EXISTS `tb_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单id',
  `order_sn` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `product_id` bigint(20) DEFAULT NULL,
  `product_name` varchar(64) DEFAULT NULL,
  `product_sn` varchar(64) DEFAULT NULL,
  `product_price` decimal(10,2) DEFAULT NULL COMMENT '销售价格',
  `product_quantity` int(11) DEFAULT NULL COMMENT '购买数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='来自同一家商店订单包含的商品表';

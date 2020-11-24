CREATE TABLE IF NOT EXISTS `tb_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(64) DEFAULT NULL COMMENT '手机号码',
  `status` int(1) DEFAULT NULL COMMENT '帐号启用状态:0->禁用;1->启用',
  `icon` varchar(500) DEFAULT NULL COMMENT '头像路径',
  `gender` int(1) DEFAULT NULL COMMENT '性别：0->火星人;1->男;2->女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `modify_time` datetime DEFAULT NULL COMMENT '最近修改时间',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`),
  UNIQUE KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `tb_order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `business_id` bigint(20) NOT NULL COMMENT '商家id',
  `order_sn` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10,2) DEFAULT NULL COMMENT '应付金额',
  `promotion_amount` decimal(10,2) DEFAULT NULL COMMENT '促销优惠金额（促销价）',
  `status` int(1) DEFAULT NULL COMMENT '订单状态：0->待付款;1->已完成;2->已关闭;3->无效订单',
  `note` varchar(500) DEFAULT NULL COMMENT '订单备注',
  `delete_status` int(1) NOT NULL DEFAULT '0' COMMENT '删除状态：0->未删除;1->已删除',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime DEFAULT NULL COMMENT '提交时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';

#订单表
#订单ID
#流水号
#订单用户
#订单商家
#订单状态
#商品快照id
#创建时间
#修改时间


CREATE TABLE IF NOT EXISTS 'tb_business'(
  `business_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商家id',
  PRIMARY KEY (`business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商家表';

CREATE TABLE IF NOT EXISTS `tb_product` (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(64) NOT NULL,
  `product_category_name` varchar(255) DEFAULT NULL COMMENT '商品分类名称',
  `product_sn` varchar(64) NOT NULL COMMENT '货号',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `sale` int(11) DEFAULT NULL COMMENT '销量',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `promotion_price` decimal(10,2) DEFAULT NULL COMMENT '促销价格',
  `description` text COMMENT '商品简述',
  `pic` varchar(255) DEFAULT NULL COMMENT '图片路径',
  `stock` int(11) DEFAULT NULL COMMENT '库存',
  `low_stock` int(11) DEFAULT NULL COMMENT '库存预警值',
  `unit` varchar(16) DEFAULT NULL COMMENT '单位',
  `detail_title` varchar(255) DEFAULT NULL COMMENT '详情标题',
  `detail_desc` text COMMENT '产品详情内容',
  `promotion_start_time` datetime DEFAULT NULL COMMENT '促销开始时间',
  `promotion_end_time` datetime DEFAULT NULL COMMENT '促销结束时间',
  `promotion_per_limit` int(11) DEFAULT NULL COMMENT '活动限购数量',
  `promotion_type` int(1) DEFAULT NULL COMMENT '促销类型：0->原价; 1->使用促销价;',
  `delete_status` int(1) DEFAULT NULL COMMENT '删除状态：0->未删除; 1->已删除',
  `publish_status` int(1) DEFAULT NULL COMMENT '上架状态：0->下架; 1->上架',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime DEFAULT NULL COMMENT '上架时间',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品信息';

CREATE TABLE IF NOT EXISTS `tb_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单id',
  `order_sn` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `product_id` bigint(20) DEFAULT NULL,
  `product_pic` varchar(500) DEFAULT NULL,
  `product_name` varchar(200) DEFAULT NULL,
  `product_brand` varchar(200) DEFAULT NULL,
  `product_sn` varchar(64) DEFAULT NULL,
  `product_price` decimal(10,2) DEFAULT NULL COMMENT '销售价格',
  `product_quantity` int(11) DEFAULT NULL COMMENT '购买数量',
  `product_category_id` bigint(20) DEFAULT NULL COMMENT '商品分类id',
  `promotion_name` varchar(200) DEFAULT NULL COMMENT '商品促销名称',
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '该商品经过优惠后的分解金额',
  `product_attr` varchar(500) DEFAULT NULL COMMENT '商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8 COMMENT='订单中所包含的商品';

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin; COMMENT='订单表';
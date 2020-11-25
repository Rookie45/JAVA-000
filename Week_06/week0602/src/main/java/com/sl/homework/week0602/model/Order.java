package com.sl.homework.week0602.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {
    private Long orderId;
    private String orderSN;
    private Long userId;
    private Long businessId;
    private String productSnapshot;
    private BigDecimal payAmount;
    private Integer orderStatus;
    private String note;
    private Integer deleteStatus;
    private Timestamp paymentTime;
    private Timestamp modifyTime;
    private Timestamp createTime;
}

package com.sl.java00.homework.separatedb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String orderSn;
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

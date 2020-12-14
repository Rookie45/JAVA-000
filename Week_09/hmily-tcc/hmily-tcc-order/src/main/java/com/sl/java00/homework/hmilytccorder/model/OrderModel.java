package com.sl.java00.homework.hmilytccorder.model;

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

    private Integer id;
    private Integer orderId;
    private String orderSn;
    private Integer orderStatus;
    private Integer productId;
    private String productName;
    private String productSn;
    private Integer productQuantity;
    private Integer skuId;
    private String skuCode;
    private Timestamp modifyTime;
    private Timestamp createTime;

}

package com.sl.java00.homework.hmilytccorder.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    NOT_PAY(0, "待付款"),

    PAYING(1, "支付中"),

    PAY_FAIL(2, "支付失败"),

    PAY_SUCCESS(3, "支付成功");

    private final int code;

    private final String desc;
}

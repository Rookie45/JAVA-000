package com.sl.java00.homework.hmilytcccommon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BalanceType {

    RMB(1, "人民币"),

    USD(2, "美元");

    private final int code;

    private final String desc;
}

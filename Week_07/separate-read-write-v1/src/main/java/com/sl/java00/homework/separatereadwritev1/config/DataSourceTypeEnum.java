package com.sl.java00.homework.separatereadwritev1.config;

public enum DataSourceTypeEnum {

    MASTER("master"),

    SLAVE("slave"),

    SLAVE1("slave1"),

    SLAVE2("slave2");

    private String name;

    DataSourceTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

package com.sl.homework.week0602.datasource;

public class ConstantParams {
    // 数据库驱动
    public static final String driver = "com.mysql.jdbc.Driver";

    // 数据库连接地址
    public static final String jdbcUrl = "jdbc:mysql://localhost:3306/mall_db?useSSL=false&charset=utf8&serverTimezone=UTC";

    // 数据库用户名
    public static final String user = "root";

    // 数据库密码
    public static final String passwd = "123456";

    // 连接池初始化大小
    public static final int initialSize = 5;

    // 连接池最小空闲
    public static final int minPoolSize = 20;

    // 连接池最大连接数量
    public static final int maxPoolSize = 100;

    // 最小逐出时间，120秒
    public static final int maxIdleTime = 120000;

    // 连接失败重试次数
    public static final int retryAttempts = 10;

    // 当连接池连接耗尽时获取连接数
    public static final int acquireIncrement = 5;

    public static final int count = 1000000;
}

package com.sl.homework.week0602.datasource;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidDataSourceCrud {

    private static DruidDataSource getDruidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(ConstantParams.driver);
        druidDataSource.setUrl(ConstantParams.jdbcUrl);
        druidDataSource.setUsername(ConstantParams.user);
        druidDataSource.setPassword(ConstantParams.passwd);
        druidDataSource.setInitialSize(ConstantParams.initialSize);
        druidDataSource.setMaxActive(ConstantParams.maxPoolSize);
        druidDataSource.setMaxWait(ConstantParams.maxIdleTime);
        druidDataSource.setDefaultAutoCommit(false);
        druidDataSource.setTestWhileIdle(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestOnBorrow(false);
        return druidDataSource;
    }
}

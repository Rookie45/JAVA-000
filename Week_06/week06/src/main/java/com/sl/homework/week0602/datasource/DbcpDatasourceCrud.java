package com.sl.homework.week0602.datasource;

import org.apache.commons.dbcp2.BasicDataSource;

public class DbcpDatasourceCrud {

    private static BasicDataSource getBasicDataSource() {
        BasicDataSource dbcpDatasource = new BasicDataSource();
        dbcpDatasource.setDriverClassName(ConstantParams.driver);
        dbcpDatasource.setUrl(ConstantParams.jdbcUrl);
        dbcpDatasource.setUsername(ConstantParams.user);
        dbcpDatasource.setPassword(ConstantParams.passwd);
        dbcpDatasource.setInitialSize(ConstantParams.initialSize);
        dbcpDatasource.setMaxIdle(ConstantParams.maxIdleTime);
        dbcpDatasource.setMaxWaitMillis(2 * ConstantParams.maxIdleTime);
        dbcpDatasource.setAutoCommitOnReturn(false);
        return dbcpDatasource;
    }
}

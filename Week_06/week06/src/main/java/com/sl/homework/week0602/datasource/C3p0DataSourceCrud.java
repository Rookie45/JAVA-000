package com.sl.homework.week0602.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;

public class C3p0DataSourceCrud {

    private static ComboPooledDataSource getC3P0PooledDataSource() {
        ComboPooledDataSource c3p0DataSource = new ComboPooledDataSource();
        try {
            c3p0DataSource.setDriverClass(ConstantParams.driver);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        c3p0DataSource.setJdbcUrl(ConstantParams.jdbcUrl);
        c3p0DataSource.setUser(ConstantParams.user);
        c3p0DataSource.setPassword(ConstantParams.passwd);
        c3p0DataSource.setInitialPoolSize(ConstantParams.initialSize);
        c3p0DataSource.setMinPoolSize(ConstantParams.minPoolSize);
        c3p0DataSource.setMaxPoolSize(ConstantParams.maxPoolSize);
        c3p0DataSource.setMaxIdleTime(ConstantParams.maxIdleTime);
        c3p0DataSource.setAutoCommitOnClose(false);
        c3p0DataSource.setTestConnectionOnCheckin(false);
        c3p0DataSource.setTestConnectionOnCheckout(false);
        return c3p0DataSource;
    }
}

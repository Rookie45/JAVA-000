package com.sl.homework.week0602.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataSourceMain {

    // 数据库驱动
    static final String driver = "com.mysql.jdbc.Driver";

    // 数据库连接地址
    static final String jdbcUrl = "jdbc:mysql://localhost:3306/demo?useSSL=false&charset=utf8&serverTimezone=UTC";

    // 数据库用户名
    static final String user = "root";

    // 数据库密码
    static final String passwd = "123456";

    // 连接池初始化大小
    static final int initialSize = 5;

    // 连接池最小空闲
    static final int minPoolSize = 20;

    // 连接池最大连接数量
    static final int maxPoolSize = 100;

    // 最小逐出时间，120秒
    static final int maxIdleTime = 120000;

    // 连接失败重试次数
    static final int retryAttempts = 10;

    // 当连接池连接耗尽时获取连接数
    static final int acquireIncrement = 5;

    static final int count = 1000000;

    static String insertSql = "INSERT INTO tb_order "+
            "(order_id,order_sn,user_id,business_id,product_snapshot,pay_amount,order_status,note,delete_status,payment_time,modify_time,create_time)"+
            "VALUES( ?, ?, ?, ?, ?, 130.00, 1, ?, 0, now(), now(), now()) ;";

    public static void main(String[] args) {

    }

    private static void hikariMillionInsert() {
        DataSource dataSource = getHikariDataSource();
//        String insertSql = "insert into tb_order (id, name) values (?,?)";
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(insertSql)) {
            for (int i=0; i < count; i++) {

            }
            // 批处理一次，不能提交百万，必须分批提交，
            // 这是因为max_allowed_packet控制器客户端向服务器端传递的最大数据量
            ps.addBatch();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static HikariDataSource getHikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(driver);
        hikariDataSource.setJdbcUrl(jdbcUrl);
        hikariDataSource.setUsername(user);
        hikariDataSource.setPassword(passwd);
        hikariDataSource.setMinimumIdle(minPoolSize);
        hikariDataSource.setMaximumPoolSize(maxPoolSize);
        hikariDataSource.setIdleTimeout(maxIdleTime);
        hikariDataSource.setAutoCommit(false);
        return hikariDataSource;
    }

    private static DruidDataSource getDruidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(jdbcUrl);
        druidDataSource.setUsername(user);
        druidDataSource.setPassword(passwd);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMaxActive(maxPoolSize);
        druidDataSource.setMaxWait(maxIdleTime);
        druidDataSource.setDefaultAutoCommit(false);
        druidDataSource.setTestWhileIdle(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestOnBorrow(false);
        return druidDataSource;
    }

    private static ComboPooledDataSource getC3P0PooledDataSource() {
        ComboPooledDataSource c3p0DataSource = new ComboPooledDataSource();
        try {
            c3p0DataSource.setDriverClass(driver);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        c3p0DataSource.setJdbcUrl(jdbcUrl);
        c3p0DataSource.setUser(user);
        c3p0DataSource.setPassword(passwd);
        c3p0DataSource.setInitialPoolSize(initialSize);
        c3p0DataSource.setMinPoolSize(minPoolSize);
        c3p0DataSource.setMaxPoolSize(maxPoolSize);
        c3p0DataSource.setMaxIdleTime(maxIdleTime);
        c3p0DataSource.setAutoCommitOnClose(false);
        c3p0DataSource.setTestConnectionOnCheckin(false);
        c3p0DataSource.setTestConnectionOnCheckout(false);
        return c3p0DataSource;
    }

    private static BasicDataSource getBasicDataSource() {
        BasicDataSource dbcpDatasource = new BasicDataSource();
        dbcpDatasource.setDriverClassName(driver);
        dbcpDatasource.setUrl(jdbcUrl);
        dbcpDatasource.setUsername(user);
        dbcpDatasource.setPassword(passwd);
        dbcpDatasource.setInitialSize(initialSize);
        dbcpDatasource.setMaxIdle(maxIdleTime);
        dbcpDatasource.setMaxWaitMillis(2*maxIdleTime);
        dbcpDatasource.setAutoCommitOnReturn(false);
        return dbcpDatasource;
    }
}

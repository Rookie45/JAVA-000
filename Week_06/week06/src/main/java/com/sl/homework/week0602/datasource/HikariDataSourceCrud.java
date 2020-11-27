package com.sl.homework.week0602.datasource;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HikariDataSourceCrud {

    //total times:76000
    public static void hikariMillionInsert(String insertSql) {

        DataSource dataSource = getHikariDataSource();
        long begin = System.currentTimeMillis();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertSql)) {
            for (long i = 1; i <= ConstantParams.count; i++) {
                ps.setLong(1, i);
                ps.setString(2, "geekbang-order-" + i);
                ps.setLong(3, i);
                ps.setLong(4, i);
                ps.setString(5, "resource\\geekbang-snapshot-\\" + i);
                ps.setString(6, "u.geekbang-" + i);
                ps.addBatch();
                // 批处理一次，不能提交百万，必须分批提交，
                // 这是因为max_allowed_packet控制器客户端向服务器端传递的最大数据量
                if (i % 10000 == 0) {
                    ps.executeBatch();
                    conn.commit();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            //rollback
        }
        long end = System.currentTimeMillis();
        System.out.println("Insert total times:" + (end - begin));
    }

    public static void hikariMillionUpdate(String updateSql) {
        DataSource dataSource = getHikariDataSource();
        long begin = System.currentTimeMillis();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {
            for (long i = 1; i <= ConstantParams.count; i++) {
                ps.setInt(1, 0);
                ps.addBatch();
                // 批处理一次，不能提交百万，必须分批提交，
                // 这是因为max_allowed_packet控制器客户端向服务器端传递的最大数据量
                if (i % 10000 == 0) {
                    ps.executeBatch();
                    conn.commit();
                }
            }

            conn.commit();
        }catch (SQLException e) {
            e.printStackTrace();
            //rollback
        }
        long end = System.currentTimeMillis();
        System.out.println("Update total times:" + (end - begin));
    }

    private static HikariDataSource getHikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(ConstantParams.driver);
        hikariDataSource.setJdbcUrl(ConstantParams.jdbcUrl);
        hikariDataSource.setUsername(ConstantParams.user);
        hikariDataSource.setPassword(ConstantParams.passwd);
        hikariDataSource.setMinimumIdle(ConstantParams.minPoolSize);
        hikariDataSource.setMaximumPoolSize(ConstantParams.maxPoolSize);
        hikariDataSource.setIdleTimeout(ConstantParams.maxIdleTime);
        hikariDataSource.setAutoCommit(false);
        return hikariDataSource;
    }
}

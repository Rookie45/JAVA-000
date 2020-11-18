package com.sl.java00.springboot.homework.lesson10.jdbc;


import com.sl.java00.springboot.homework.lesson10.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 1.prepareStatement()创建PreparedStatement对象
 * <p>
 * 2.设置提交属性setAutoCommit().
 * <p>
 * 3.通过addBatch()添加SQL语句，这里可以setXxx填充SQL语句中的占位符参数.
 * <p>
 * 4.调用executeBatch()创建Statement对象.
 * <p>
 * 5.最后commit()提交事务.
 * <p>
 * 6.失败rollback()回滚事务
 */
public class CustomBatchJDBC {

    public void batchInsert(List<Student> stus) {
        Connection conn = CustomJDBC.getConnection();
        String sql = "insert into tb_student (id, name) values (?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            for (int i = 0; i < stus.size(); i++) {
                ps.setInt(1, stus.get(i).getId());
                ps.setString(2, stus.get(i).getName());
                ps.addBatch();
            }
            ps.executeBatch();
//            ps.clearBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(conn);
        } finally {
            close(conn);
        }
    }

    public void batchUpdate(List<Student> stus) {
        Connection conn = CustomJDBC.getConnection();
        String sql = "update tb_student set name=? where id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            for (int i = 0; i < stus.size(); i++) {
                ps.setString(1, stus.get(i).getName());
                ps.setInt(2, stus.get(i).getId());
                ps.addBatch();
            }
            ps.executeBatch();
//            ps.clearBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(conn);
        } finally {
            close(conn);
        }
    }

    public void batchDelete(List<Student> stus) {
        Connection conn = CustomJDBC.getConnection();
        String sql = "delete from tb_student where id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            for (int i = 0; i < stus.size(); i++) {
                ps.setInt(1, stus.get(i).getId());
                ps.addBatch();
            }
            ps.executeBatch();
//            ps.clearBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(conn);
        } finally {
            close(conn);
        }
    }

    private void rollback(Connection conn) {
        if (null != conn) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void close(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


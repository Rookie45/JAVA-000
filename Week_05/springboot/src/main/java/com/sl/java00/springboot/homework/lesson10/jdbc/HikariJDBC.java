package com.sl.java00.springboot.homework.lesson10.jdbc;

import com.sl.java00.springboot.homework.lesson10.model.Student;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 本身HikariCP搭配ORM框架，比如mybatis
 * 会更好使用，这里简单化了，使用JDBC一样的使用方式
 * 获得连接、创建语句执行对象、执行sql语句、处理结果、释放资源
 * 事实上这样做事浪费的，毕竟连接池嘛
 */
@Data
@Component
public class HikariJDBC {

    @Autowired
    private DataSource dataSource;

    public void insert(Student stu) {
        Connection conn = getConnection();
        String sql = "insert into tb_student (id, name) values (?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, stu.getId());
            ps.setString(2, stu.getName());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(conn);
        } finally {
            close(conn);
        }
    }

    public void update(Student stu) {
        Connection conn = getConnection();
        String sql = "update tb_student set name=? where id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, stu.getName());
            ps.setInt(2, stu.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(conn);
        } finally {
            close(conn);
        }
    }

    public void delete(int id) {
        Connection conn = getConnection();
        String sql = "delete from tb_student where id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(conn);
        } finally {
            close(conn);
        }
    }

    public Student select(int id) {
        Student result = null;
        Connection conn = getConnection();
        String sql = "select id, name from tb_student where id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int studentId = resultSet.getInt("id");
                String studentName = resultSet.getString("name");
                result = new Student();
                result.setId(studentId);
                result.setName(studentName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(conn);
        } finally {
            close(conn);
        }
        return result;
    }

    private Connection getConnection() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
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

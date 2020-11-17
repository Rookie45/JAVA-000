package com.sl.java00.springboot.homework.lesson10.jdbc;

import com.sl.java00.springboot.homework.lesson10.model.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC操作数据库的步骤:
 * 1.加载驱动
 * 		告知JVM使用的是哪一个数据库的驱动
 * 2.获得连接
 * 		使用JDBC中的类,完成对mysql数据库的连接(TCP协议)
 * 3.创建语句执行对象
 * 		通过连接对象获取对SQL语句的执行者对象
 * 4.执行sql语句
 * 		使用执行者对象,向数据库执行SQL语句
 * 		获取数据库的执行后的结果
 * 5.处理结果
 * 6.释放资源
 * 		调用一堆close
 */
public class CustomJDBC {

    public int insert(Student student) {
        int result = 0;
        Connection conn = getConnection();
        Statement stat = null;
        try {
            stat = conn.createStatement();
            String sql = String.format("insert into tb_student (id, name) values ('%d', '%s')",
                    student.getId(), student.getName());
            result = stat.executeUpdate(sql);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public int update(Student student) {
        int result = 0;
        Connection conn = getConnection();
        Statement stat = null;
        try {
            stat = conn.createStatement();
            String sql = String.format("update tb_student set name='%s' where id='%d'",
                    student.getName(), student.getId());
            result = stat.executeUpdate(sql);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public int delete(int id) {
        int result = 0;
        Connection conn = getConnection();
        Statement stat = null;
        try {
            stat = conn.createStatement();
            String sql = String.format("delete from tb_student where id='%d'",
                    id);
            result = stat.executeUpdate(sql);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public Student select(int id) {
        Student result = null;
        Connection conn = getConnection();
        Statement stat = null;
        try {
            stat = conn.createStatement();
            String sql = String.format("select id, name from tb_student where id='%d'",
                    id);
            ResultSet resultSet = stat.executeQuery(sql);
            if (resultSet.next()) {
                int studentId = resultSet.getInt("id");
                String studentName = resultSet.getString("name");
                result = new Student(studentId, studentName);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public List<Student> selectAll() {
        List<Student> result = new ArrayList<>();
        Connection conn = getConnection();
        Statement stat = null;
        try {
            stat = conn.createStatement();
            String sql = "select id, name from tb_student";
            ResultSet resultSet = stat.executeQuery(sql);
            while (resultSet.next()) {
                int studentId = resultSet.getInt("id");
                String studentName = resultSet.getString("name");
                Student student = new Student(studentId, studentName);
                result.add(student);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

     static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo",
                    "root", "123456");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}

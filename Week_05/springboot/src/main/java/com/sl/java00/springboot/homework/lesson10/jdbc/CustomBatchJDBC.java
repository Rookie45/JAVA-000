package com.sl.java00.springboot.homework.lesson10.jdbc;

import com.sl.java00.springboot.homework.lesson10.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CustomBatchJDBC {

    public void batchInsert(List<Student> stus) {
        Connection conn = CustomJDBC.getConnection();
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            String sql = "insert into tb_student (id, name) values (?,?)";
            ps = conn.prepareStatement(sql);
            for (int i=0; i < stus.size(); i++) {
                ps.setInt(1, stus.get(i).getId());
                ps.setString(2, stus.get(i).getName());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

package com.sl.java00.springboot;

import com.sl.java00.springboot.homework.lesson10.jdbc.CustomBatchJDBC;
import com.sl.java00.springboot.homework.lesson10.jdbc.CustomHikariConfig;
import com.sl.java00.springboot.homework.lesson10.jdbc.CustomJDBC;
import com.sl.java00.springboot.homework.lesson10.jdbc.HikariJDBC;
import com.sl.java00.springboot.homework.lesson10.model.Student;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.ArrayList;

public class SpringbootApplicationTests {

    @Test
    public void jdbcInsertTest() {
        Student student1 = new Student(1, "aaa");
        CustomJDBC customJDBC = new CustomJDBC();
        customJDBC.insert(student1);
    }

    @Test
    public void jdbcUpdateTest() {
        Student student1 = new Student(1, "bbb");
        CustomJDBC customJDBC = new CustomJDBC();
        customJDBC.update(student1);
    }

    @Test
    public void jdbcSelectTest() {
        CustomJDBC customJDBC = new CustomJDBC();
        System.out.println(customJDBC.select(1));
    }

    @Test
    public void jdbcSelectAllTest() {
        CustomJDBC customJDBC = new CustomJDBC();
        System.out.println(customJDBC.selectAll());
    }

    @Test
    public void jdbcDeleteTest() {
        CustomJDBC customJDBC = new CustomJDBC();
        System.out.println(customJDBC.delete(1));
    }

    @Test
    public void jdbcBatchInsertTest() {
        Student student1 = new Student(1, "aaa");
        Student student2 = new Student(2, "bbb");
        ArrayList<Student> list = new ArrayList<>();
        list.add(student1);
        list.add(student2);
        CustomBatchJDBC batchJDBC = new CustomBatchJDBC();
        batchJDBC.batchInsert(list);
    }

    @Test
    public void jdbcBatchUpdateTest() {
        Student student1 = new Student(1, "ccc");
        Student student2 = new Student(2, "ddd");
        ArrayList<Student> list = new ArrayList<>();
        list.add(student1);
        list.add(student2);
        CustomBatchJDBC batchJDBC = new CustomBatchJDBC();
        batchJDBC.batchUpdate(list);
    }

    @Test
    public void jdbcBatchDeleteTest() {
        Student student1 = new Student(1, "ccc");
        Student student2 = new Student(2, "ddd");
        ArrayList<Student> list = new ArrayList<>();
        list.add(student1);
        list.add(student2);
        CustomBatchJDBC batchJDBC = new CustomBatchJDBC();
        batchJDBC.batchDelete(list);
    }

    @Test
    public void hikariInsertTest() {
        Student student1 = new Student(2, "bbb");
        DataSource dataSource = new CustomHikariConfig().dataSource();
        HikariJDBC hikariJDBC = new HikariJDBC();
        hikariJDBC.setDataSource(dataSource);
        hikariJDBC.insert(student1);
    }

    @Test
    public void hikariUpdateTest() {
        Student student1 = new Student(2, "ccc");
        DataSource dataSource = new CustomHikariConfig().dataSource();
        HikariJDBC hikariJDBC = new HikariJDBC();
        hikariJDBC.setDataSource(dataSource);
        hikariJDBC.update(student1);
    }

    @Test
    public void hikariSelectTest() {
        DataSource dataSource = new CustomHikariConfig().dataSource();
        HikariJDBC hikariJDBC = new HikariJDBC();
        hikariJDBC.setDataSource(dataSource);
        System.out.println(hikariJDBC.select(1));
    }


    @Test
    public void hikariDeleteTest() {
        DataSource dataSource = new CustomHikariConfig().dataSource();
        HikariJDBC hikariJDBC = new HikariJDBC();
        hikariJDBC.setDataSource(dataSource);
        hikariJDBC.delete(2);
    }
}

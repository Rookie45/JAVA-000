package com.sl.java00.homework.separatedb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.sl.java00.homework.separatedb.mapper")
public class SeparateDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeparateDbApplication.class, args);
    }

}

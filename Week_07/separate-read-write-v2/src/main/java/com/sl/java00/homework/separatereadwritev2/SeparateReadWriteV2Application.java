package com.sl.java00.homework.separatereadwritev2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.sl.java00.homework.separatereadwritev2.mapper")
public class SeparateReadWriteV2Application {

    public static void main(String[] args) {
        SpringApplication.run(SeparateReadWriteV2Application.class, args);
    }

}

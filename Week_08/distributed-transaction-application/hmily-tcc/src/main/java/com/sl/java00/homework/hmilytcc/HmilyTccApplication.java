package com.sl.java00.homework.hmilytcc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.sl.java00.homework.hmilytcc.mapper")
public class HmilyTccApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmilyTccApplication.class, args);
    }

}

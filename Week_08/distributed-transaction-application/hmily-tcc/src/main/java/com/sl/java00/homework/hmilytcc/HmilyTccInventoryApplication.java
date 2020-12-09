package com.sl.java00.homework.hmilytccinvetory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
@MapperScan("com.sl.java00.homework.hmilytccinvetory.mapper")
@ImportResource({"classpath:applicationContext.xml"})
public class HmilyTccInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmilyTccInventoryApplication.class, args);
    }

}

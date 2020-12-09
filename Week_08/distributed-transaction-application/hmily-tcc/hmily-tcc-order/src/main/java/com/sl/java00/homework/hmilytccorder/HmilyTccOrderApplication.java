package com.sl.java00.homework.hmilytccorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
@ImportResource({"classpath:spring-dubbo.xml"})
@MapperScan("com.sl.java00.homework.hmilytccorder.mapper")
public class HmilyTccOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmilyTccOrderApplication.class, args);
    }

}

package com.sl.java00.homework.hmilytccprovider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
@PropertySource("classpath:dubbo-provider.properties")
@ImportResource({"classpath:spring-dubbo.xml"})
@MapperScan("com.sl.java00.homework.hmilytcccommon.mapper")
public class HmilyTccProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmilyTccProviderApplication.class, args);
    }

}

package com.sl.homework.lesson09.spring.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.sl.homework.lesson09.spring.bean")
public class StudentConfig {

    @Bean(name = "studentByBean")
    public Student student() {
        return Student.createByStatic();
    }

}

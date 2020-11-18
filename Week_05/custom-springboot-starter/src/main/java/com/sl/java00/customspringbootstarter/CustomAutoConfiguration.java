package com.sl.java00.customspringbootstarter;

import com.sl.java00.customspringbootstarter.properties.Klass;
import com.sl.java00.customspringbootstarter.properties.School;
import com.sl.java00.customspringbootstarter.properties.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({Student.class, School.class, Klass.class})
@RequiredArgsConstructor
@ComponentScan("com.sl.java00.customspringbootstarter")
public class CustomAutoConfiguration {
    private final Student student;
    private final School school;
    private final Klass class1;

}

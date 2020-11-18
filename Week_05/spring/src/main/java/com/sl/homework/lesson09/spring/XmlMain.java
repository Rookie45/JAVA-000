package com.sl.homework.lesson09.spring.xml;

import com.sl.homework.lesson09.spring.bean.Student;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlMain {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:custom-xml-bean.xml");
        Student stu10 = applicationContext.getBean("stu10", Student.class);
        System.out.println(stu10);
    }
}

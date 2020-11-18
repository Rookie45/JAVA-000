package com.sl.homework.lesson09.spring.xml;

import com.sl.homework.lesson09.spring.bean.Student;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Creating new XML configuration extensions can be done by following these (relatively) simple steps:
 *
 * Authoring an XML schema to describe your custom element(s).
 * Coding a custom NamespaceHandler implementation (this is an easy step, don’t worry).
 * Coding one or more BeanDefinitionParser implementations (this is where the real work is done).
 * Registering the above artifacts with Spring (this too is an easy step).
 */
public class XmlMain {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:custom-xml-bean.xml");
        Student stu10 = applicationContext.getBean("stu10", Student.class);
        System.out.println(stu10);
    }
}

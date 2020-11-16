package com.sl.homework.lesson09.spring.bean;

import org.springframework.beans.factory.FactoryBean;

public class StudentFactoryBean implements FactoryBean<Student> {
    @Override
    public Student getObject() throws Exception {
        return Student.createByStatic();
    }

    @Override
    public Class<?> getObjectType() {
        return Student.class;
    }
}

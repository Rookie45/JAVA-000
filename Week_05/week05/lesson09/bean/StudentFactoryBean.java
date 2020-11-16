package com.sl.java00.week05.lesson09.bean;

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

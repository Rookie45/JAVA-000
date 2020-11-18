package com.sl.homework.lesson09.spring.xml;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class CustomStudentHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("student", new CustomStudentParser());
    }
}

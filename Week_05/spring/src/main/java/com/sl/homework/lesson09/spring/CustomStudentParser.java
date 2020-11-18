package com.sl.homework.lesson09.spring.xml;

import com.sl.homework.lesson09.spring.bean.Student;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class CustomStudentParser extends  AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return Student.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String id = element.getAttribute("stuId");
        String name = element.getAttribute("name");
        builder.addPropertyValue("id", id)
                .addPropertyValue("name", name);
    }
}

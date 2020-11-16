package com.sl.homework.lesson09.spring.bean;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component(value = "studentByResource")
public class ResourceAnnotationStudent {

    // 按照ByName自动注入，J2EE下的javax.annotation.Resource。
    // @Resource有两个重要的属性：name和type，而Spring将@Resource注解的name属性解析为bean的名字，而type属性则解析为bean的类型。
    @Resource(name = "studentByComponent")
    private ComponentAnnotationStudent studentBean;

    public Student getStudent() {
        return studentBean.getStudent();
    }
}

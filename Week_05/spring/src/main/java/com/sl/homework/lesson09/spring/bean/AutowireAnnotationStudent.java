package com.sl.homework.lesson09.spring.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "studentByAutowire")
public class AutowireAnnotationStudent {

    // 按照类型（byType）装配依赖对象，默认情况下它要求依赖对象必须存在，
    // 如果允许null值，可以设置它的required属性为false。如果按照名称（byName）来装配，可以结合@Qualifier注解一起使用
    @Autowired
    private ComponentAnnotationStudent studentBean;

    /*@Autowired
    public AutowireAnnotationStudent(ComponentAnnotationStudent componentStudentBean) {
        this.studentBean = componentStudentBean;
    }*/

    public Student getStudent() {
        return studentBean.getStudent();
    }
}

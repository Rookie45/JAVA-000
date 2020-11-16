package com.sl.homework.lesson09.spring.bean;

import org.springframework.stereotype.Component;

@Component(value = "studentByComponent")
public class ComponentAnnotationStudent {

    private Student student;

    public ComponentAnnotationStudent() {
        student = Student.createByStatic();
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}

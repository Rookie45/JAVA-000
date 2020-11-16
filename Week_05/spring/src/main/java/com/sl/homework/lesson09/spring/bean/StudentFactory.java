package com.sl.homework.lesson09.spring.bean;

public interface StudentFactory {
    default Student create(){
        return Student.createByStatic();
    }
}

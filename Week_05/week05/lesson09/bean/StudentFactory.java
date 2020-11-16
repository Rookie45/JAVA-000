package com.sl.java00.week05.lesson09.bean;

public interface StudentFactory {
    default Student create(){
        return Student.createByStatic();
    }
}

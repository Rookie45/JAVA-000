package com.sl.homework.lesson09.spring.aop;

public class DefaultISchool implements ISchool {
    @Override
    public void ding() {
        System.out.println("开始上课...");
    }
}

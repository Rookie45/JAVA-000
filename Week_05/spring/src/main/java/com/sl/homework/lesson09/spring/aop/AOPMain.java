package com.sl.homework.lesson09.spring.aop;

public class AOPMain {
    public static void main(String[] args) {
        JDKProxy2AOP jdkProxy2AOP = new JDKProxy2AOP();
        jdkProxy2AOP.setTarget(new DefaultISchool());
        jdkProxy2AOP.setBeforeAdvice(() -> System.out.println("老师好！"));
        jdkProxy2AOP.setAfterAdvice(() -> System.out.println("老师再见！"));
        ISchool proxySchool = (ISchool) jdkProxy2AOP.creatProxy();
        proxySchool.ding();

        CGLibProxy2AOP cgLibProxy2AOP = new CGLibProxy2AOP();
        cgLibProxy2AOP.setTarget(new DefaultISchool());
        cgLibProxy2AOP.setBeforeAdvice(() -> System.out.println("老师好！"));
        cgLibProxy2AOP.setAfterAdvice(() -> System.out.println("老师再见！"));
        ISchool cglibSchool = (ISchool) cgLibProxy2AOP.creatProxy();
        cglibSchool.ding();
    }
}

package com.sl.java00.week05.lesson09.bean;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class BeanMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:student-bean.xml");
        Student studentByConstructor = applicationContext.getBean("student-by-constructor", Student.class);
        Student studentBySetter = applicationContext.getBean("student-by-setter", Student.class);
        Student studentByStaticMethod = applicationContext.getBean("student-by-static-method", Student.class);
        Student studentByInstanceMethod = applicationContext.getBean("student-by-instance-method", Student.class);
        Student studentByFactoryBean = applicationContext.getBean("student-by-factory-bean", Student.class);
//        ServiceLoader<StudentFactory> serviceLoader = applicationContext.getBean("student-by-service-loader", ServiceLoader.class);
//        methodByServiceLoader(serviceLoader);
        System.out.println(studentByConstructor);
        System.out.println(studentBySetter);
        System.out.println(studentByStaticMethod);
        System.out.println(studentByInstanceMethod);
        System.out.println(studentByFactoryBean);
        System.out.println(studentByFactoryBean);
        methodByAutowireCapableBeanFactory(applicationContext);
//        ((ClassPathXmlApplicationContext) applicationContext).close();
    }

    /*static void methodByServiceLoader(ServiceLoader<StudentFactory> serviceLoader) {
        Iterator<StudentFactory> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            StudentFactory studentFactory = iterator.next();
            System.out.println(studentFactory.create());
        }
    }*/

    static void methodByAutowireCapableBeanFactory(ApplicationContext applicationContext) {
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        StudentFactory studentFactory = beanFactory.createBean(DefaultStudentFactory.class);
        System.out.println(studentFactory.create());
    }
}

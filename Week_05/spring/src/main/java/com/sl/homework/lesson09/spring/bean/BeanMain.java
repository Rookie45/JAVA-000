package com.sl.homework.lesson09.spring.bean;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanMain {

    public static void main(String[] args) {
        assembleBeanByAnnotation();
        assembleBeanByXML();
    }

    static void assembleBeanByAnnotation() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(StudentConfig.class);
        applicationContext.refresh();

        methodByBeanAnnotation(applicationContext);
        methodByComponentAnnotation(applicationContext);
        methodByAutowireAnnotation(applicationContext);
        methodByResourceAnnotation(applicationContext);
//        System.out.println("define bean："+String.join(",", applicationContext.getBeanDefinitionNames()));
        applicationContext.close();
    }

    static void methodByResourceAnnotation(AnnotationConfigApplicationContext applicationContext) {
        ResourceAnnotationStudent componentBean = applicationContext.getBean("studentByResource", ResourceAnnotationStudent.class);
        System.out.println(componentBean.getStudent());
    }

    static void methodByAutowireAnnotation(AnnotationConfigApplicationContext applicationContext) {
        AutowireAnnotationStudent componentBean = applicationContext.getBean("studentByAutowire", AutowireAnnotationStudent.class);
        System.out.println(componentBean.getStudent());
    }

    static void methodByComponentAnnotation(AnnotationConfigApplicationContext applicationContext) {
        ComponentAnnotationStudent componentBean = applicationContext.getBean("studentByComponent", ComponentAnnotationStudent.class);
        System.out.println(componentBean.getStudent());
    }

    static void methodByBeanAnnotation(AnnotationConfigApplicationContext applicationContext) {
        Student student = applicationContext.getBean("studentByBean", Student.class);
        System.out.println(student);
    }

    static void assembleBeanByXML() {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:student-bean.xml");

        methodByConstructorXML(applicationContext);
        methodBySetterXML(applicationContext);
        methodByStaticMethodXML(applicationContext);
        methodByInstanceMethodXML(applicationContext);
        methodByFactoryBean(applicationContext);
//        methodByServiceLoader(applicationContext.getBean("student-by-service-loader", ServiceLoader.class));
        methodByAutowireCapableBeanFactory(applicationContext);
        applicationContext.close();
    }

    static void methodByConstructorXML(ApplicationContext applicationContext) {
        Student studentByConstructor = applicationContext.getBean("student-by-constructor", Student.class);
        System.out.println(studentByConstructor);
    }

    static void methodBySetterXML(ApplicationContext applicationContext) {
        Student studentBySetter = applicationContext.getBean("student-by-setter", Student.class);
        System.out.println(studentBySetter);
    }

    static void methodByStaticMethodXML(ApplicationContext applicationContext) {
        Student studentByStaticMethod = applicationContext.getBean("student-by-static-method", Student.class);
        System.out.println(studentByStaticMethod);
    }

    static void methodByInstanceMethodXML(ApplicationContext applicationContext) {
        Student studentByInstanceMethod = applicationContext.getBean("student-by-instance-method", Student.class);
        System.out.println(studentByInstanceMethod);
    }

    static void methodByFactoryBean(ApplicationContext applicationContext) {
        Student studentByFactoryBean = applicationContext.getBean("student-by-factory-bean", Student.class);
        System.out.println(studentByFactoryBean);
    }

    /*resources下需要特别设置一个配置文件
    static void methodByServiceLoader(ServiceLoader<StudentFactory> serviceLoader) {
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

1、使Java里的动态代理，实现一个简单的AOP。
2、写代码实现Spring Bean的装配，方式越多越好（XML、Annotation都可以）,提
交到Github。
3、实现一个Spring XML自定义配置，配置一组Bean，例如Student/Klass/School 

```
pom.xml需要配置
  <groupId>com.sl.java00</groupId>
  <artifactId>spring</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>war</packaging>
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <spring.version>5.2.2.RELEASE</spring.version>
  </properties>
  
    <dependency>
      <groupId>commons-io</groupId>
      <artifactId>commons-io</artifactId>
      <version>2.6</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-core</artifactId>
      <version>${spring.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>${spring.version}</version>
    </dependency>
      <dependency>
          <groupId>org.projectlombok</groupId>
          <artifactId>lombok</artifactId>
          <version>1.18.12</version>
      </dependency>
    <dependency>
    
student-bean.xml配置    
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="student-by-constructor" class="com.sl.homework.lesson09.spring.bean.Student">
        <constructor-arg name="id" value="2"/>
        <constructor-arg name="name" value="shili"/>
    </bean>

    <bean id="student-by-setter" class="com.sl.homework.lesson09.spring.bean.Student">
        <property name="id" value="2"/>
        <property name="name" value="shili"/>
    </bean>

    <bean id="student-by-static-method" class="com.sl.homework.lesson09.spring.bean.Student"
     factory-method="createByStatic"/>

    <bean id="student-by-instance-method" factory-bean="studentFactory" factory-method="create"/>
    <bean id="studentFactory" class="com.sl.homework.lesson09.spring.bean.DefaultStudentFactory"/>

    <bean id="student-by-factory-bean" class="com.sl.homework.lesson09.spring.bean.StudentFactoryBean"/>

    <!--<bean id="student-by-service-loader" class="org.springframework.beans.factory.serviceloader.ServiceFactoryBean">-->
        <!--<property name="serviceType" value="com.sl.java00.week05.lesson09.bean.StudentFactory"/>-->
     <!--</bean>-->
</beans>

package com.sl.homework.lesson09.spring.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student implements Serializable {
    
    private int id;
    private String name;
    
    public void init(){
        System.out.println("hello...........");
    }
    
    public Student create(){
        return new Student(2,"shili");
    }

    public static Student createByStatic(){
        return new Student(2,"shili");
    }
}

package com.sl.homework.lesson09.spring.bean;

public interface StudentFactory {
    default Student create(){
        return Student.createByStatic();
    }
}

package com.sl.homework.lesson09.spring.bean;

public class DefaultStudentFactory implements StudentFactory {
}

package com.sl.homework.lesson09.spring.bean;

import org.springframework.beans.factory.FactoryBean;

public class StudentFactoryBean implements FactoryBean<Student> {
    @Override
    public Student getObject() throws Exception {
        return Student.createByStatic();
    }

    @Override
    public Class<?> getObjectType() {
        return Student.class;
    }
}
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


package com.sl.homework.lesson09.spring.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.sl.homework.lesson09.spring.bean")
public class StudentConfig {

    @Bean(name = "studentByBean")
    public Student student() {
        return Student.createByStatic();
    }

}

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


    

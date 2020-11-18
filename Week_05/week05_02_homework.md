**题目**
1. 总结一下，单例的各种写法，比较它们的优劣。

**答题如下**

[singleton](https://github.com/Rookie45/JAVA-000/tree/main/Week_05/springboot/src/main/java/com/sl/java00/springboot/homework/lesson10/singleton)

**题目**

2. maven/spring的profile机制，都有什么用法？

**答题如下**

maven的profile用于打包时，指定使用哪个profile环境进行打包运行，该profile除了在pom.xml配置外，还可以在setting.xml里配置

spring的profile用于指定程序运行时使用哪个application配置文件，比方说test配置，dev配置以及prod配置

**题目**

3. 给前面课程提供的Student/Klass/School实现自动配置和Starter。

**答题如下**

[custom-springboot-starter](https://github.com/Rookie45/JAVA-000/tree/main/Week_05/custom-springboot-starter)

**题目**

4. 总结Hibernate与MyBatis的各方面异同点。

**答题如下**

- Mybatis
  - 优点：原生SQL（XML语法），直观，对DBA友好
  - 缺点：繁琐，可以用MyBatis-generator、MyBatis-Plus之类的插件弥补
- Hibernate
  - 优点：简单场景不用写SQL（HQL、Cretiria、SQL）
  - 缺点：对DBA不友好 
  

其他比较：
1. hibernate是全自动，而mybatis是半自动。
2. hibernate数据库移植性远大于mybatis。
3. hibernate拥有完整的日志系统，mybatis则欠缺一些。
4. mybatis相比hibernate需要关心很多细节
5. sql直接优化上，mybatis要比hibernate方便很多

**题目**

5. 学习MyBatis-generator的用法和原理，学会自定义TypeHandler处理复杂类型。

**答题如下**

[handler](https://github.com/Rookie45/JAVA-000/tree/main/Week_05/springboot/src/main/java/com/sl/java00/springboot/homework/lesson10/handler)

**题目**

6. 研究一下JDBC接口和数据库连接池，掌握它们的设计和用法：
   1. 使用JDBC原生接口，实现数据库的增删改查操作。
   2. 使用事务，PrepareStatement方式，批处理方式，改进上述操作。
   3. 配置Hikari连接池，改进上述操作。提交代码到Github  

**答题如下**

[jdbc](https://github.com/Rookie45/JAVA-000/tree/main/Week_05/springboot/src/main/java/com/sl/java00/springboot/homework/lesson10/jdbc)

**参考**

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)

[Using Transactions](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html)

[HikariCP](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby)

[Configuration Metadata](https://docs.spring.io/spring-boot/docs/2.1.7.RELEASE/reference/html/configuration-metadata.html#configuration-metadata-format)

[springboot之additional-spring-configuration-metadata.json自定义提示](https://www.cnblogs.com/Purgeyao/p/11439555.html)

[springboot 自定义starter的过程以及遇到的问题](https://www.codenong.com/jsc63b8d1dead8/)

[单例模式几种实现方式](https://www.cnblogs.com/ngy0217/p/9006716.html)

[mybatis与hibernate区别](https://blog.csdn.net/wangpeng047/article/details/17038659)

[MyBatis GeneratorXML Configuration File Reference](http://mybatis.org/generator/configreference/xmlconfig.html)

[generator-demo](https://github.com/geektime-geekbang/geektime-spring-family/tree/master/Chapter%203/mybatis-generator-demo)

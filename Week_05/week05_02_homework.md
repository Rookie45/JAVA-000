**题目**
1. 总结一下，单例的各种写法，比较它们的优劣。

**答题如下**

[singleton]()

**题目**

2. maven/spring的profile机制，都有什么用法？

**答题如下**

**题目**

3. 给前面课程提供的Student/Klass/School实现自动配置和Starter。

**答题如下**
[custom-springboot-starter]()

**题目**

4. 总结Hibernate与MyBatis的各方面异同点。

**答题如下**

- Mybatis
  - 优点：原生SQL（XML语法），直观，对DBA友好
  - 缺点：繁琐，可以用MyBatis-generator、MyBatis-Plus之类的插件
- Hibernate
  - 优点：简单场景不用写SQL（HQL、Cretiria、SQL）
  - 缺点：对DBA不友好 

**题目**

5. 学习MyBatis-generator的用法和原理，学会自定义TypeHandler处理复杂类型。

**答题如下**

**题目**

6. 研究一下JDBC接口和数据库连接池，掌握它们的设计和用法：
   1. 使用JDBC原生接口，实现数据库的增删改查操作。
   2. 使用事务，PrepareStatement方式，批处理方式，改进上述操作。
   3. 配置Hikari连接池，改进上述操作。提交代码到Github  

**答题如下**

[jdbc]()

**参考**

[Using Transactions](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html)

[HikariCP](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby)

[Configuration Metadata](https://docs.spring.io/spring-boot/docs/2.1.7.RELEASE/reference/html/configuration-metadata.html#configuration-metadata-format)

[springboot之additional-spring-configuration-metadata.json自定义提示](https://www.cnblogs.com/Purgeyao/p/11439555.html)

[springboot 自定义starter的过程以及遇到的问题](https://www.codenong.com/jsc63b8d1dead8/)

[单例模式几种实现方式](https://www.cnblogs.com/ngy0217/p/9006716.html)

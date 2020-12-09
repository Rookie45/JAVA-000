- **问题现象：**

```java
org.dromara.hmily.config.api.exception.ConfigException: ConfigLoader:loader config error,error file path:null
...
```

问题原因：

hmily的配置文件与预期不符合，应该是hmily.yml，要求名称和后缀都得一致，不能写成hmily.yaml

- **问题现象：**

```java
org.yaml.snakeyaml.scanner.ScannerException: mapping values are not allowed here
 in 'reader', line 7, column 17:
          serializer: kryo
```

问题原因：

hmily.yml的serializer: kryo与官方访问格式上有出入，多出了空格。

- **问题现象：**

```java
com.mongodb.MongoSocketOpenException: Exception opening socket
```

问题原因：

hmily.yml中配置了mongodb相关的信息，所以springboot自动加载机制，尝试加载mongodb的配置类，而实际环境中没有mongodb相关信息，只需要加载时排查掉mongodb的自动配置类`@SpringBootApplication(exclude = {MongoAutoConfiguration.class})`

- **问题现象：**

```java
A component required a bean named 'inventoryService' that could not be found.
```

问题原因：

@Service默认以类名作为Bean的名称，该类名为`InventoryServiceImpl`，而在spring-dubbo.xml里配置的是`inventoryService`，可以`@Service(value = "inventoryService")`指定Bean名称

- **问题现象：**

```java
java.lang.ArrayIndexOutOfBoundsException: 0
```

问题原因：

HmilyTccTransactionExecutor类的buildHmilyParticipant方法，通过clazz.getInterfaces()[0]反射获取confirm和cancel方法对应类的接口时，需要该类存在接口

- **问题现象：**

```java
java.lang.RuntimeException: org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.binding.BindingException: Parameter 'productId' not found. Available parameters are [id, param1]
```

问题原因：

下面这个语句，@param没有和Mapper文件中定义的#{}对应上

```java
InventoryModel selectByProductId(@Param("productId") Integer productId);
```

- **问题现象：**

```java
com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near '>
```

问题原因：

XML文件多余了“>”字符

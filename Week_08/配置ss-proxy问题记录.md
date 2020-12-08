编译ss，使用如下命令可大大减短编译时间

```java
mvn clean install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Drat.skip=true -Dcheckstyle.skip=true
```

其中shardingsphere-integration-test这个模块编译时，不能跳过test，所以单独编译这个模块，mvn命令不能带有-Dmaven.test.skip=true



启动ss-proxy时抛错：

```java
[INFO ] 15:43:21.603 [main] o.apache.curator.utils.Compatibility - Using org.apa
che.zookeeper.server.quorum.MultipleAddresses
Exception in thread "main" java.lang.NullPointerException
        at org.apache.shardingsphere.infra.metadata.model.physical.model.table.P
hysicalTableMetaDataLoader.load(PhysicalTableMetaDataLoader.java:50)
        at org.apache.shardingsphere.sharding.metadata.ShardingMetaDataLoader.lo
ad(ShardingMetaDataLoader.java:84)
        at org.apache.shardingsphere.sharding.metadata.ShardingMetaDataLoader.lo
ad(ShardingMetaDataLoader.java:67)
        at org.apache.shardingsphere.sharding.metadata.ShardingMetaDataLoader.lo
ad(ShardingMetaDataLoader.java:55)
        at org.apache.shardingsphere.infra.metadata.model.logic.LogicSchemaMetaD
ataLoader.load(LogicSchemaMetaDataLoader.java:72)
        at org.apache.shardingsphere.infra.context.schema.SchemaContextsBuilder.
createMetaData(SchemaContextsBuilder.java:108)
        at org.apache.shardingsphere.infra.context.schema.SchemaContextsBuilder.
createSchema(SchemaContextsBuilder.java:102)
        at org.apache.shardingsphere.infra.context.schema.SchemaContextsBuilder.
build(SchemaContextsBuilder.java:93)
        at org.apache.shardingsphere.proxy.initializer.impl.AbstractBootstrapIni
tializer.createSchemaContexts(AbstractBootstrapInitializer.java:75)
        at org.apache.shardingsphere.proxy.initializer.impl.AbstractBootstrapIni
tializer.init(AbstractBootstrapInitializer.java:63)
        at org.apache.shardingsphere.proxy.Bootstrap.main(Bootstrap.java:48)
请按任意键继续. . .
```

定位原因：

问题在于actualDataNodes的数据库集，映射的不是mall_0，mall_1，而是ds_0，ds_1

```
...
dataSources:
  ds_0:
    url: jdbc:mysql://127.0.0.1:3306/mall_0?serverTimezone=UTC&useSSL=false
  ds_1:
    url: jdbc:mysql://127.0.0.1:3306/mall_1?serverTimezone=UTC&useSSL=false
#虚拟表为t_order，真实表为t_order_${0..15}
rules:
- !SHARDING
  tables:
    t_order:
      actualDataNodes: ds_${0..1}.t_order_${0..15}
      tableStrategy:
        standard:
          shardingColumn: id
          shardingAlgorithmName: t_order_inline
      keyGenerateStrategy:
        column: id
        keyGeneratorName: snowflake
...        
```



登陆ss-proxy

```mysql
PS D:\programs\apache> mysql -h127.0.0.1 -P3307 -uroot -p123456
mysql: [Warning] Using a password on the command line interface can be insecure.
ERROR 2005 (HY000): Unknown MySQL server host '127' (2)
PS D:\programs\apache> mysql -h 127.0.0.1 -P3307 -uroot -p123456
mysql: [Warning] Using a password on the command line interface can be insecure.
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 1
Server version: 5.7.25-ShardingSphere-Proxy 5.0.0-RC1

Copyright (c) 2000, 2019, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql>
```



问题现象：

创建时，只有一张表

原因：

在ss-proxy里创建的表名与配置里的逻辑表名不一致导致



问题现象：

```java
java.sql.SQLException: 2Unknown exception: [Cannot use mod() on this number type: java.math.BigDecimal
```

原因是algorithm-expression使用除法时，使用id/16，ss-proxy采用的是groovy表达式，id/16会以小数存储，而我的期望是只要商，所以使用groovy函数intdiv()取整

```
  shardingAlgorithms:
    database_inline:
      type: INLINE
      props:
        algorithm-expression: ds_${ id % 2 }
    t_order_inline:
      type: INLINE
      props:
        algorithm-expression: t_order_${(id.intdiv(16) + id) % 16}
```


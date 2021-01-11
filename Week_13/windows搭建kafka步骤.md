### wind7搭建单机kafka步骤

1. 下载kafka软件包

   [kafka_2.12-2.7.0.tgz](https://www.apache.org/dyn/closer.cgi?path=/kafka/2.7.0/kafka_2.12-2.7.0.tgz)

2. 启动kafka

   找到解压ActiveMQ所在目录，进入bin目录，shift+右击，选择“在此处打开命令窗口”，执行命令启动zookeeper

   ```java
   D:\program\apache\kafka_2.12-2.7.0\bin>windows\zookeeper-server-start.bat ..\config\zookeeper.properties
   ```

   运行输出内容

   ```java
   ...
   [2021-01-10 16:27:00,904] INFO Configuring NIO connection handler with 10s sessi
   onless connection timeout, 2 selector thread(s), 16 worker threads, and 64 kB di
   rect buffers. (org.apache.zookeeper.server.NIOServerCnxnFactory)
   [2021-01-10 16:27:00,915] INFO binding to port 0.0.0.0/0.0.0.0:2181 (org.apache.
   zookeeper.server.NIOServerCnxnFactory)
   [2021-01-10 16:27:00,961] INFO zookeeper.snapshotSizeFactor = 0.33 (org.apache.z
   ookeeper.server.ZKDatabase)
   [2021-01-10 16:27:00,976] INFO Snapshotting: 0x0 to \tmp\zookeeper\version-2\sna
   pshot.0 (org.apache.zookeeper.server.persistence.FileTxnSnapLog)
   [2021-01-10 16:27:00,998] INFO Snapshotting: 0x0 to \tmp\zookeeper\version-2\sna
   pshot.0 (org.apache.zookeeper.server.persistence.FileTxnSnapLog)
   [2021-01-10 16:27:01,064] INFO Using checkIntervalMs=60000 maxPerMinute=10000 (o
   rg.apache.zookeeper.server.ContainerManager)
   ```

   相同目录下再开一个窗口，shift+右击，选择“在此处打开命令窗口”，执行命令启动kafka

   ```java
   D:\program\apache\kafka_2.12-2.7.0\bin>windows\kafka-server-start.bat ..\config\server.properties
   ```

   运行输出内容

   ```java
   ...
   [2021-01-10 16:30:12,556] INFO Kafka version: 2.7.0 (org.apache.kafka.common.uti
   ls.AppInfoParser)
   [2021-01-10 16:30:12,557] INFO Kafka commitId: 448719dc99a19793 (org.apache.kafk
   a.common.utils.AppInfoParser)
   [2021-01-10 16:30:12,560] INFO Kafka startTimeMs: 1610267412532 (org.apache.kafk
   a.common.utils.AppInfoParser)
   [2021-01-10 16:30:12,563] INFO [KafkaServer id=0] started (kafka.server.KafkaSer
   ver)
   [2021-01-10 16:30:12,661] INFO [broker-0-to-controller-send-thread]: Recorded ne
   w controller, from now on will use broker 0 (kafka.server.BrokerToControllerRequ
   estThread)
   ```

3. 创建一个topic

   相同目录下再开一个窗口，shift+右击，选择“在此处打开命令窗口”，执行

   ```java
   D:\program\apache\kafka_2.12-2.7.0\bin>.\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic kafka-my-topic
   Created topic kafka-my-topic.
   ```

4. 查询kafka内的topic

   相同目录下再开一个窗口，shift+右击，选择“在此处打开命令窗口”，执行

   ```java
   D:\program\apache\kafka_2.12-2.7.0\bin>.\windows\kafka-topics.bat --list --zooke
   eper localhost:2181
   kafka-my-topic
   ```

5. 启动生产者和消费者

   相同目录下再开一个窗口，shift+右击，选择“在此处打开命令窗口”，生产者执行

   ```java
   D:\program\apache\kafka_2.12-2.7.0\bin>.\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic kafka-my-topic
   >hello kafka
   ```

   相同目录下再开一个窗口，shift+右击，选择“在此处打开命令窗口”，消费者执行

   ```java
   D:\program\apache\kafka_2.12-2.7.0\bin>.\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --from-beginning --topic kafka-my-topic
   hello kafka
   ```

   
### wind7搭建集群kafka步骤

1. 环境准备

    如果基于上述环境继续搭建，需要先暂停运行的kafka，清除目前的配置信息，避免干扰。可以使用[zooinspector](https://github.com/zzhang5/zooinspector)连上启动的zookeeper，然后通过zooinspector界面清除配置信息。仅保留zookeeper目录，其余全部删除。然后是日志文件也得删除，日志默认路径在`log.dirs=/tmp/kafka-logs`

2. 配置准备

   将/config/server.properties配置文件复制三份，分别命名为server9091.properties，server9092.properties，server9093.properties，修改每份文件的三处配置，如下所示，broker.id的值，log.dris的日志目录，listeners的监听url。

   ```java
   ...
   broker.id=1
   log.dirs=/tmp/kafka/kafka-logs1
   listeners = PLAINTEXT://localhost:9091
   ...
   ```

   ```java
   ...
   broker.id=2
   log.dirs=/tmp/kafka/kafka-logs2
   listeners = PLAINTEXT://localhost:9092
   ...
   ```

   ```java
   ...
   broker.id=3
   log.dirs=/tmp/kafka/kafka-logs3
   listeners = PLAINTEXT://localhost:9093
   ...
   ```

3. 逐一启动集群节点，下面以9091节点为例

    ```java
    D:\program\apache\kafka_2.12-2.7.0\bin>windows\kafka-server-start.bat ..\config\server9091.properties
    ```

4. 创建3分区2副本的topic并查询分配情况

    ```java
    D:\program\apache\kafka_2.12-2.7.0\bin>.\windows\kafka-topics.bat --create --zoo
    keeper localhost:2181 --topic my-cluster-topic --partitions 3 --replication-fact
    or 2
    Created topic my-cluster-topic.
    
    D:\program\apache\kafka_2.12-2.7.0\bin>.\windows\kafka-topics.bat --describe --z
    ookeeper localhost:2181 --topic  my-cluster-topic
    Topic: my-cluster-topic PartitionCount: 3       ReplicationFactor: 2    Configs:
    
            Topic: my-cluster-topic Partition: 0    Leader: 2       Replicas: 2,3
    Isr: 2,3
            Topic: my-cluster-topic Partition: 1    Leader: 3       Replicas: 3,1
    Isr: 3,1
            Topic: my-cluster-topic Partition: 2    Leader: 1       Replicas: 1,2
    Isr: 1,2
    ```
    
    依据上面查询的结果，可以整理出来节点(node)与分区(partition)的关系，这里Leader也是一种特殊的副本(Replicas)，Isr为同步信息。

    
    |            | node9091 | node9092 | node9093 |
    | ---------- | -------- | -------- | -------- |
    | Partition0 |          | Leader   | Replicas |
    | Partition1 | Replicas |          | Leader   |
| Partition2 | Leader   | Replicas |          |
    



**参考**

[kafka quickstart](https://kafka.apache.org/documentation/#quickstart)

[在Windows下安装使用Kafka](https://www.jianshu.com/p/ce203d4e2f41)

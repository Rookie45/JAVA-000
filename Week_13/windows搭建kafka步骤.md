### wind7搭建kafka步骤

1. 下载kafka软件包

   [kafka_2.12-2.7.0.tgz](https://www.apache.org/dyn/closer.cgi?path=/kafka/2.7.0/kafka_2.12-2.7.0.tgz)

2. 启动kafka

   找到解压ActiveMQ所在目录，进入bin目录，shift+右击，选择“在此处打开命令窗口”，执行

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

   相同目录下再开一个窗口，shift+右击，选择“在此处打开命令窗口”，执行

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

6. 集群搭建

   。。。



**参考**

[kafka quickstart](https://kafka.apache.org/documentation/#quickstart)

[在Windows下安装使用Kafka](https://www.jianshu.com/p/ce203d4e2f41)

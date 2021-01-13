
常见MQ

**RabbitMQ**

**ZeroMQ**

**ActiveMQ**

**Kafka/Jafka**



# kafka设计解析

kafka是一种分布式的，基于发布/订阅的消息系统。

### 设计目标

- 以时间复杂度为 O(1) 的方式提供消息持久化能力，即使对 TB 级以上数据也能保证常数时间复杂度的访问性能。
- 高吞吐率。即使在非常廉价的商用机器上也能做到单机支持每秒 100K 条以上消息的传输。
- 支持 Kafka Server 间的消息分区，及分布式消费，同时保证每个 Partition 内的消息顺序传输。
- 同时支持离线数据处理和实时数据处理。
- Scale out：支持在线水平扩展。

### 特点

1. 解耦两端的处理过程
2. 保证数据的可靠性处理，遵从“插入 - 获取 - 删除”范式
3. 具有极好的扩展性，即两端消息生产和消费可以在不改变代码和参数情况下扩大
4. 具有偶发的峰值处理能力
5. 可恢复性，在某个处理消息的进程挂掉后，加入队列的消息仍能在系统恢复后继续被处理
6. 顺序性，保证一个Partition内消息的有序性
7. 缓冲能力
8. 异步通信能力

### 基本概念

- **Broker**：kafka集群中的服务器节点，每个服务器称为一个broker
- **Topic**：发布到kafka集群中的消息的类别（物理上不同 Topic 的消息分开存储，逻辑上一个 Topic 的消息虽然保存于一个或多个 broker 上但用户只需指定消息的 Topic 即可生产或消费数据而不必关心数据存于何处）
- **Partition**：节点上的分区，物理上的概念，每个topic包含一个或多个partition
- **Producer**：负责发送消息到broker
- **Consumer**：负责从broker读取消息（客户端）
- **Consumer Group**：每个Consumer属于某个特定的Consumer Group（可为每个 Consumer 指定 group name，若不指定 group name 则属于默认的 group）

一个典型的 Kafka 集群中包含：

- 若干 Producer（可以是 web 前端产生的 Page View，或者是服务器日志，系统 CPU、Memory 等）

- 若干 broker（Kafka 支持水平扩展，一般 broker 数量越多，集群吞吐率越高）

- 若干 Consumer Group

- 一个Zookeeper集群。

Kafka 通过 Zookeeper 管理集群配置，选举 leader，以及在 Consumer Group 发生变化时进行 rebalance。Producer 使用 push 模式将消息发布到 broker，Consumer 使用 pull 模式从 broker 订阅并消费消息。

### Topic与Partition

Topic 在逻辑上可以被认为是一个 queue，每条消费都必须指定它的 Topic，可以简单理解为必须指明把这条消息放进哪个 queue 里。

为了使得 Kafka 的吞吐率可以线性提高，物理上把 Topic 分成一个或多个 Partition，每个 Partition 在物理上对应一个文件夹，该文件夹下存储这个 Partition 的所有消息和索引文件。
前面讲到**一个partition里消息是有顺序的，在消息被append到一个partition时，属于顺序写磁盘，因此效率很高**，这也说明一点，如果是含有磁头的磁盘，partition数不宜过多，因为不同partition需要移动磁头，频繁移动磁头相当于随机写，效率极低。

### 日志文件

每个日志文件都是一个 log entrie 序列，每个 log entrie 包含一个 4 字节整型数值（值为 N+5），1 个字节的"magic value"，4 个字节的 CRC 校验码，其后跟 N 个字节的消息体。每条消息都有一个当前 Partition 下唯一的 64 字节的 offset，它指明了这条消息的起始位置。磁盘上存储的消息格式如下：

```
message length ： 4 bytes (value: 1+4+n)
"magic" value ： 1 byte 
crc ： 4 bytes 
payload ： n bytes 
```

这个 log entries 并非由一个文件构成，而是分成多个 segment，每个 segment 以该 segment 第一条消息的 offset 命名并以“.kafka”为后缀。另外会有一个索引文件，它标明了每个 segment 下包含的 log entry 的 offset 范围。（此条信息是否过时，待验证）

### 消息保留策略

对于传统的 message queue 而言，一般会删除已经被消费的消息，而 Kafka 集群会保留所有的消息，无论其被消费与否。当然，因为磁盘限制，不可能永久保留所有数据（实际上也没必要），因此 Kafka 提供两种策略删除旧数据。一是基于时间，二是基于 Partition 文件大小，通过配置/config/server.properties文件

```java
# The minimum age of a log file to be eligible for deletion
log.retention.hours=168
# The maximum size of a log segment file. When this size is reached a new log segment will be created.
log.segment.bytes=1073741824
# The interval at which log segments are checked to see if they can be deleted according to the retention policies
log.retention.check.interval.ms=300000
# If log.cleaner.enable=true is set the cleaner will be enabled and individual logs can then be marked for log compaction.
log.cleaner.enable=false

```

Kafka 会为每一个 Consumer Group 保留一些 metadata 信息——当前消费的消息的 position，也即 offset。这个 offset 由 Consumer 控制。正常情况下 Consumer 会在消费完一条消息后递增该 offset。当然，Consumer 也可将 offset 设成一个较小的值，重新消费一些消息。因为 offet 由 Consumer 控制，所以Kafka broker 是无状态的，它不需要标记哪些消息被哪些消费过，也不需要通过 broker 去保证同一个 Consumer Group 只有一个 Consumer 能消费某一条消息，因此也就不需要锁机制，这也为 Kafka 的高吞吐率提供了有力保障。**新版本（0.9之后）更改offset存储位置，由原本存在Zookeeper里改为存储到Kafka内部topic里。**

### Producer消息路由

Producer 发送消息到 broker 时，会根据 Paritition 机制选择将其存储到哪一个 Partition，这里可以使用负载均衡。如果一个 Topic 对应一个文件，那这个文件所在的机器 I/O 将会成为这个 Topic 的性能瓶颈，而有了 Partition 后，不同的消息可以并行写入不同 broker 的不同 Partition 里，极大的提高了吞吐率。可以在 $KAFKA_HOME/config/server.properties 中通过配置项 num.partitions 来指定新建 Topic 的默认 Partition 数量，也可在创建 Topic 时通过参数指定，同时也可以在 Topic 创建之后通过 Kafka 提供的工具修改。

在发送一条消息时，可以指定这条消息的 key，Producer 根据这个 key 和 Partition 机制来判断应该将这条消息发送到哪个 Parition。Paritition 机制可以通过指定 Producer 的 paritition. class 这一参数来指定，该 class 必须实现 kafka.producer.Partitioner 接口。（此条信息是否过时，待验证）

### Consumer Group

**同一 Topic 的一条消息只能被同一个 Consumer Group 内的一个 Consumer 消费，但多个 Consumer Group 可同时消费这一消息。**这是 Kafka 用来实现一个 Topic 消息的广播（发给所有的 Consumer）和单播（发给某一个 Consumer）的手段。一个 Topic 可以对应多个 Consumer Group。如果需要实现广播，只要每个 Consumer 有一个独立的 Group 就可以了。要实现单播只要所有的 Consumer 在同一个 Group 里。用 Consumer Group 还可以将 Consumer 进行自由的分组而不需要多次发送消息到不同的 Topic。

一个组内的Consumer实例共享一个公共的ID，即group ID。组内所有Consumer实例一起消费（订阅的）Topic的所有Partition，每个Partition只能由同组里某个Consumer实例消费（不同组的Consumer实例当然也能消费该Partition）。

Kafka 的设计理念之一就是同时提供离线处理和实时处理。根据这一特性，可以使用 Storm 这种实时流处理系统对消息进行实时在线处理，同时使用 Hadoop 这种批处理系统进行离线处理，还可以同时将数据实时备份到另一个数据中心，只需要保证这三个操作所使用的 Consumer 属于不同的 Consumer Group 即可。

#### consumer group状态机

状态有如下五种

- Dead：组内已经没有任何成员的最终状态，组的元数据也已经被coordinator移除了。这种状态响应各种请求都是一个response： UNKNOWN_MEMBER_ID

- Empty：组内无成员，但是位移信息还没有过期。这种状态只能响应JoinGroup请求

- PreparingRebalance：组准备开启新的rebalance，等待成员加入

- AwaitingSync：正在等待leader consumer将分配方案传给各个成员

- Stable：rebalance完成！可以开始消费了

  相互之间的关系如下：

  ```mermaid
    graph LR
  B[Empty] -->A[Dead]
  C[PreparingRebalance] -->A[Dead]
  D[AwaitingSync] -->A[Dead]
  E[Stable] -->A[Dead]
  B[Empty] -->C[PreparingRebalance]
  C[PreparingRebalance] -->B[Empty]
  C[PreparingRebalance] -->D[AwaitingSync]
  D[AwaitingSync] -->C[PreparingRebalance]
  D[AwaitingSync] -->E[Stable]
  E[Stable] -->C[PreparingRebalance]
  
  ```



### Consumer Offset

每个consumer group保存自己的位移信息，那么只需要简单的一个整数表示位置就够了；同时可以引入checkpoint机制定期持久化，简化了应答机制的实现。Kafka默认是定期自动提交offset(enable.auto.commit = true)，你当然可以选择手动提交位移实现自己控制。另外kafka会定期把group消费情况保存起来，做成一个offset map。

老版本的offset是提交到zookeeper中的，目录结构是：/consumers/<group.id>/offsets/<topic>/<partitionId>，但是zookeeper其实并不适合进行大批量的读写操作，尤其是写操作。因此kafka提供了另一种解决方案：增加**\__consumer\__offsets topic**，将offset信息写入这个topic，摆脱对zookeeper的依赖(指保存offset这件事情)。_consumer_offsets中的消息保存了每个consumer group某一时刻提交的offset信息，包括group.id，Topic+Partition以offset

### Push VS Pull

push 模式很难适应消费速率不同的消费者，因为消息发送速率是由 broker 决定的。push 模式的目标是尽可能以最快速度传递消息，但是这样很容易造成 Consumer 来不及处理消息，典型的表现就是拒绝服务以及网络拥塞。而 pull 模式则可以根据 Consumer 的消费能力以适当的速率消费消息。

对于 Kafka 而言，pull 模式更合适。pull 模式可简化 broker 的设计，Consumer 可自主控制消费消息的速率，同时 Consumer 可以自己控制消费方式——即可批量消费也可逐条消费，同时还能选择不同的提交方式从而实现不同的传输语义。



### Rebalance的问题

Rebalance本质是一种协议，规定一个Consumer Group下所有Consumer如何达成一致，来分配订阅Topic的每个分区。比如某个Group下有20个Consumer实例，订阅了一个具有100个分区的Topic。正常情况下，Kafka平均会为每个Consumer分配5个分区，这个分配过程叫Rebalance。

Rebalance发生时，会让所有Consumer实例停止消费，等待Rebalance完成，类似JVM的STW，而这个Rebalance的过程非常漫长。

触发Rebalance的条件有3个：

-  Group的成员发生变化，比如有Consumer实例加入或者离开Group。
-  订阅topic数发生变化，Consumer Group可以使用正则表达式的方式订阅topic，比如consumer.subscribe(Pattern.compile("s.*l"))，它表示该Group订阅所有以字母s开头，l结尾的topic。在Group运行过程中，新创建了一个满足这个条件的topic，该Group就会发生Rebalance
-  订阅topic的分区数发生变化，Kafka当前只允许增加一个topic的分区数，当该分区数增加，就会触发订阅该topic的所有Group发生Rebalance

Rebalance发生时，Group下所有Consumer实例都需要参与，Consumser消费topic的哪些分区，当前Kafka默认提供了3种策略（可以自定义）：

1. range，
2. round-robin，
3. 

#### coordinator

Rebalance由coordinator这样一个角色来执行和对Consumer Group管理，这个group coordinator承担的责任比如组成员管理、位移提交保护机制等。当consumer group的第一个consumer启动的时候，它会去和kafka server确定谁是它们组的coordinator。确认方式：

1. 首先确认consumer group位移信息写入\__consumer\__offsets的哪个分区，计算方式如下：

   \__consumer\__offsets partition=Math.abs(groupId.hashCode() % groupMetadataTopicPartitionCount);

   groupMetadataTopicPartitionCount由offset.topic.num.partitions指定

2. 然后该分区的leader所在的broker就被选为coordinator

#### Generation

表示Rebalance之后的一届成员，主要是用于保护consumer group，隔离无效offset提交的。比如上一届的consumer成员是无法提交位移到新一届的consumer group中。每次group进行rebalance之后，generation号都会加1，表示group进入到了一个新的版本。

#### protocol

Rebalance本质是一组协议，group与coordinator共同使用它来完成group的rebalance。目前kafka提供了5个协议来处理与consumer group coordination相关的问题：

- Heartbeat请求：consumer需要定期给coordinator发送心跳来表明自己还活着
- LeaveGroup请求：主动告诉coordinator我要离开consumer group
- SyncGroup请求：group leader把分配方案告诉组内所有成员
- JoinGroup请求：成员请求加入组
- DescribeGroup请求：显示组的所有信息，包括成员信息，协议名称，分配方案，订阅信息等。通常该请求是给管理员使用

#### Rebalance过程

前提是coordinator已经确定，Rebalance过程分为两步：

1. Join

   所有成员向coordinator发生JoinGroup请求，申请加入Group，一旦所有成员发生完JoinGroup请求，coordinator会从中选取一个consumer担任leader，并把成员信息以及订阅信息发送给leader，leader负责消费分配方案的制定。

2. Sync

   leader开始分配消费方案，即Group中哪个consumer消费哪些Topic里的哪些partition。一旦完成分配，leader会将这个方案封装进SyncGroup请求中发给coordinator，非leader也会发SyncGroup请求，只是内容为空。coordinator接收到分配方案之后会把方案塞进SyncGroup的response中发给各个consumer。这样组内的所有成员就都知道自己应该消费哪些分区了。

> consumer group的分区分配方案是在客户端执行的，这样做可以有更好的灵活性





**参考**

[Kafka设计解析（一）：Kafka背景及架构介绍](https://www.infoq.cn/article/kafka-analysis-part-1/)

[Kafka设计解析（二）：Kafka High Availability （上）](https://www.infoq.cn/article/kafka-analysis-part-2?utm_source=related_read&utm_medium=article)

[Kafka设计解析（三）：Kafka High Availability （下）](https://www.infoq.cn/article/kafka-analysis-part-3/?utm_source=infoq&utm_medium=related_content_link&utm_campaign=relatedContent_articles_clk)

[Kafka设计解析（四）：Kafka Consumer解析](https://www.infoq.cn/article/kafka-analysis-part-4/?hmsr=toutiao.io&utm_campaign=infoq_content&utm_medium=toutiao.io&utm_source=toutiao.io&utm_term=global)

[Kafka设计解析（五）：Kafka Benchmark](https://www.infoq.cn/article/kafka-analysis-part-5)

[Kafka设计解析（六）：Kafka高性能关键技术解析](https://www.infoq.cn/article/kafka-analysis-part-6?utm_source=related_read_bottom&utm_medium=article)

[Kafka设计解析（七）：流式计算的新贵 Kafka Stream](https://www.infoq.cn/article/kafka-analysis-part-7)

[Kafka设计解析（八）：Kafka事务机制与Exactly Once语义实现原理](https://www.infoq.cn/article/kafka-analysis-part-8)

[Kafka设计解析（九）为何去掉replica.lag.max.messages参数](https://www.cnblogs.com/warehouse/p/9534007.html)

[Kafka设计解析（十）Kafka如何创建topic](https://www.cnblogs.com/warehouse/p/9534230.html)

...一系列

[关于kafka配额讨论](https://www.cnblogs.com/huxi2b/p/8609453.html)

[The Internals of Apache Kafka 2.4.0](https://jaceklaskowski.gitbooks.io/apache-kafka/content/)

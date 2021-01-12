**题目**

1、 （必做） 搭建一个3节点Kafka集群， 测试功能和性能； 实现spring kafka下对kafka集群的操作， 将代码提交到github。

**答题如下**

[spring-kafka](https://github.com/Rookie45/JAVA-000/tree/main/Week_13/spring-kafka)

2、 （选做） 安装kafka-manager工具， 监控kafka集群状态。

3、 （挑战☆） 演练本课提及的各种生产者和消费者特性。

4、 （挑战☆☆☆） Kafka金融领域实战： 在证券或者外汇、 数字货币类金融核心交易系统里，对于订单的处理， 大概可以分为收单、 定

序、 撮合、 清算等步骤。 其中我们一般可以用mq来实现订单定序， 然后将订单发送给撮合模块。

1） 收单： 请实现一个订单的rest接口， 能够接收一个订单Order对象；

2） 定序： 将Order对象写入到kafka集群的order.usd2cny队列， 要求数据有序并且不丢失；

3） 撮合： 模拟撮合程序（不需要实现撮合逻辑） ， 从kafka获取order数据， 并打印订单信息，要求可重放, 顺序消费, 消息仅处理一次。  



**参考**

[SpringBoot + KafKa集群的集成](https://segmentfault.com/a/1190000019733221)

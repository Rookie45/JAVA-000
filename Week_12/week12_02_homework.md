**题目**

1、 （必做） 搭建ActiveMQ服务， 基于JMS， 写代码分别实现对于queue和topic的消息生产和消费， 代码提交到github。

[]()

2、 （选做） 基于数据库的订单表， 模拟消息队列处理订单：

1） 一个程序往表里写新订单， 标记状态为未处理(status=0);

2） 另一个程序每隔100ms定时从表里读取所有status=0的订单， 打印一下订单数据，然后改成完成status=1；

3） （挑战☆） 考虑失败重试策略， 考虑多个消费程序如何协作。

3、 （选做） 将上述订单处理场景， 改成使用ActiveMQ发送消息处理模式。

4、 （选做） 使用java代码， 创建一个ActiveMQ Broker Server， 并测试它。  

5、 （挑战☆☆） 搭建ActiveMQ的network集群和master-slave主从结构。

6、 （挑战☆☆☆） 基于ActiveMQ的MQTT实现简单的聊天功能或者Android消息推送。

7、 （挑战☆） 创建一个RabbitMQ， 用Java代码实现简单的AMQP协议操作。

8、 （挑战☆☆） 搭建RabbitMQ集群， 重新实现前面的订单处理。

9、 （挑战☆☆☆） 使用Apache Camel打通上述ActiveMQ集群和RabbitMQ集群， 实现所有写入到ActiveMQ上的一个队列q24的消息， 自动转发到RabbitMQ。

10、 （挑战☆☆☆） 压测ActiveMQ和RabbitMQ的性能。  

**答题如下**



**参考**

[SpringBoot结合ActiveMQ（同时支持Queue和Topic）](https://segmentfault.com/a/1190000011190467)

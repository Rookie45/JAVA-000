1、 基于内存Queue实现生产和消费API（已经完成）

1） 创建内存Queue， 作为底层消息存储

2） 定义Topic， 支持多个Topic

3） 定义Producer， 支持Send消息

4） 定义Consumer， 支持Poll消息

2、 去掉内存Queue， 设计自定义Queue， 实现消息确认和消费offset

1） 自定义内存Message数组模拟Queue。

2） 使用指针记录当前消息写入位置。

3） 对于每个命名消费者， 用指针记录消费位置。

3、 拆分broker和client(包括producer和consumer)
1） 将Queue保存到web server端
2） 设计消息读写API接口， 确认接口， 提交offset接口
3） producer和consumer通过httpclient访问Queue
4） 实现消息确认， offset提交
5） 实现consumer从offset增量拉取

4、 增加多种策略（各条之间没有关系， 可以任意选择实现）
1） 考虑实现消息过期， 消息重试， 消息定时投递等策略
2） 考虑批量操作， 包括读写， 可以打包和压缩
2） 考虑消息清理策略， 包括定时清理， 按容量清理等
3） 考虑消息持久化， 存入数据库， 或WAL日志文件， 或BookKeeper
4） 考虑将spring mvc替换成netty下的tcp传输协议

5、 对接各种技术（各条之间没有关系， 可以任意选择实现）
1） 考虑封装 JMS 1.1 接口规范
2） 考虑实现 STOMP 消息规范
3） 考虑实现消息事务机制与事务管理器
4） 对接Spring
5） 对接Camel或Spring Integration
6） 优化内存和磁盘的使用

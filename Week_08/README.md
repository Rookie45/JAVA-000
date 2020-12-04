学习笔记



**zookeeper参数说明**

- tickTime: ZooKeeper 中使用的基本时间单元, 以毫秒为单位, 默认值是 2000。它用来调节心跳和超时。例如, 默认的会话超时时间是两倍的 tickTime。

- initLimit: 默认值是 10, 即 tickTime 属性值的 10 倍。它用于配置允许 followers 连接并同步到 leader 的最大时间。如果 ZooKeeper 管理的数据量很大的话可以增加这个值。

- syncLimit: 默认值是 5, 即 tickTime 属性值的 5 倍。它用于配置leader 和 followers 间进行心跳检测的最大延迟时间。如果在设置的时间内 followers 无法与 leader 进行通信, 那么 followers 将会被丢弃。

- dataDir: ZooKeeper 用来存储内存数据库快照的目录, 并且除非指定其它目录, 否则数据库更新的事务日志也将会存储在该目录下。建议配置 dataLogDir 参数来指定 ZooKeeper 事务日志的存储目录。

- clientPort: 服务器监听客户端连接的端口, 也即客户端尝试连接的端口, 默认值是 2181。

- maxClientCnxns: 在 socket 级别限制单个客户端与单台服务器之前的并发连接数量, 可以通过 IP 地址来区分不同的客户端。它用来防止某种类型的 DoS 攻击, 包括文件描述符耗尽。默认值是 60。将其设置为 0 将完全移除并发连接数的限制。

- autopurge.snapRetainCount: 配置 ZooKeeper 在自动清理的时候需要保留的数据文件快照的数量和对应的事务日志文件, 默认值是 3。

- autopurge.purgeInterval: 和参数 autopurge.snapRetainCount 配套使用, 用于配置 ZooKeeper 自动清理文件的频率, 默认值是 1, 即默认开启自动清理功能, 设置为 0 则表示禁用自动清理功能。



**参考**

[]()
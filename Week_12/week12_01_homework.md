**题目**

1、 （必做） 配置redis的主从复制， sentinel高可用， Cluster集群。提交如下内容到github：

1） config配置文件，

2） 启动和操作、 验证集群下数据读写的命令步骤。

**答题如下：**

建立主从复制，有如下三种方式：

（1）配置文件

在从服务器的配置文件中加入：slaveof <masterip> <masterport>

（2）启动命令

redis-server启动命令后加入 --slaveof <masterip> <masterport>

（3）客户端命令

Redis服务器启动后，直接通过客户端执行命令：slaveof <masterip> <masterport>，则该Redis实例成为从节点。



sentinel高可用，主宕机后，从升为主

```
127.0.0.1:26379> info sentinel
# Sentinel
sentinel_masters:1
sentinel_tilt:0
sentinel_running_scripts:0
sentinel_scripts_queue_length:0
sentinel_simulate_failure_flags:0
master0:name=mymaster,status=ok,address=127.0.0.1:6379,slaves=2,sentinels=3
127.0.0.1:26379> info sentinel
# Sentinel
sentinel_masters:1
sentinel_tilt:0
sentinel_running_scripts:0
sentinel_scripts_queue_length:0
sentinel_simulate_failure_flags:0
master0:name=mymaster,status=ok,address=127.0.0.1:6381,slaves=2,sentinels=3
```





**题目**

2、 （选做） 练习示例代码里下列类中的作业题：

08cache/redis/src/main/java/io/kimmking/cache/RedisApplication.java

**答题如下：**



**题目**

3、 （挑战☆） 练习redission的各种功能；

**答题如下：**



**题目**

4、 （挑战☆☆） 练习hazelcast的各种功能；

**答题如下：**



**题目**

5、 （挑战☆☆☆） 搭建hazelcast 3节点集群， 写入100万数据到一个map， 模拟和演示高可用， 测试一下性能；  

**答题如下：**







**参考**

[深入学习Redis（3）：主从复制](https://www.cnblogs.com/kismetv/p/9236731.html)

[深入学习Redis（4）：哨兵](https://www.cnblogs.com/kismetv/p/9609938.html)

[深入学习Redis（5）：集群](https://www.cnblogs.com/kismetv/p/9853040.html)


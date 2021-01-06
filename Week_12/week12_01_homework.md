**题目**

1、 （必做） 配置redis的主从复制， sentinel高可用， Cluster集群。提交如下内容到github：

1） config配置文件

[主从配置](https://github.com/Rookie45/JAVA-000/tree/main/Week_12/主从配置)

[sentinel配置](https://github.com/Rookie45/JAVA-000/tree/main/Week_12/sentinel配置)

[集群配置](https://github.com/Rookie45/JAVA-000/tree/main/Week_12/集群配置)

2） 启动和操作、 验证集群下数据读写的命令步骤。

**答题如下：**

建立主从复制（一主一从），有如下三种方式：

1. 配置文件

   在从服务器的配置文件中加入：slaveof <masterip> <masterport>

2. 启动命令

   redis-server启动命令后加入 --slaveof <masterip> <masterport>

3. 客户端命令

   Redis服务器启动后，直接通过客户端执行命令：slaveof <masterip> <masterport>，则该Redis实例成为从节点。



建立sentinel高可用（1主2从3哨兵）

依次启动主节点master.6379.conf和两个从节点slave1.6380.conf，slave2.6381.conf，命令：`redis-server.exe <配置文件名>`

在启动哨兵节点，命令：`redis-server.exe <配置文件名> --sentinel`

主宕机后，从升为主

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



建立Cluster集群（3主3从）

1. 启动节点

   将六个节点配置开启cluster-enabled，依次启动3台主节点，3台从节点，命令：`redis-server.exe <配置文件名>`

2. 建立握手

   使用命令：`cluster meet <其他节点ip> <端口>`，依次将其余节点加入到集群中，另外可以使用cluster nodes查看集群中节点情况

3. 分配槽

   使用命令：`cluster addslots <槽编号>`，槽取值为[0,16383]，当所有槽分配完成，集群进入OK状态

4. 指定主从关系

   使用命令：`cluster replicate <节点id>`，这里节点id可以用`cluster nodes`命令查询，第一列即节点id，这里节点id填的是主节点

```bash
D:\program\cluster\6380>redis-cli.exe -p 6380
127.0.0.1:6380>
127.0.0.1:6380>
127.0.0.1:6380> cluster nodes
ee0c7c20bf604486340ea850372b2f19aace5175 127.0.0.1:6381 master - 0 1609825626762
 0 connected 5462-10922
ea40ad376ca3aa1e5a94c28409c90288ee98e680 127.0.0.1:6382 master - 0 1609825624762
 2 connected 10923-16383
9c54b037a8d565de8d74a44025f8d10ff35ac184 127.0.0.1:6384 slave ee0c7c20bf60448634
0ea850372b2f19aace5175 0 1609825628763 4 connected
560bbfcfae2f71f8bf6cc4dd566fe6026c3d1e85 127.0.0.1:6383 slave cbcbc5cfdf31ffe927
2f10bb2d71b623cae1810b 0 1609825625762 3 connected
64d3e091603b93ff0c5c82964fb05c28c3827c1e 127.0.0.1:6385 slave ea40ad376ca3aa1e5a
94c28409c90288ee98e680 0 1609825627762 5 connected
cbcbc5cfdf31ffe9272f10bb2d71b623cae1810b 127.0.0.1:6380 myself,master - 0 0 1 co
nnected 0-5461
127.0.0.1:6380> cluster info
cluster_state:ok
cluster_slots_assigned:16384
cluster_slots_ok:16384
cluster_slots_pfail:0
cluster_slots_fail:0
cluster_known_nodes:6
cluster_size:3
cluster_current_epoch:5
cluster_my_epoch:1
cluster_stats_messages_sent:21087
cluster_stats_messages_received:21087
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


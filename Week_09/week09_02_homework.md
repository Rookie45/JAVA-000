**题目**

1、 （选做） 按课程第二部分练习各个技术点的应用。

**答题**

**题目**

2、 （选做） 按dubbo-samples项目的各个demo学习具体功能使用。

**答题**

**题目**

3、 （必做） 结合dubbo+hmily， 实现一个TCC外汇交易处理， 代码提交到github：

- [x] 1） 用户A的美元账户和人民币账户都在A库， 使用1美元兑换7人民币；
- [x] 2） 用户B的美元账户和人民币账户都在B库， 使用7人民币兑换1美元；
- [x] 3） 设计账户表， 冻结资产表， 实现上述两个本地事务的分布式事务。

**答题**

[hmily-tcc](https://github.com/Rookie45/JAVA-000/tree/main/Week_09/hmily-tcc)

**题目**

4、 （挑战☆☆） 尝试扩展Dubbo
1） 基于上次作业的自定义序列化， 实现Dubbo的序列化扩展；
2） 基于上次作业的自定义RPC， 实现Dubbo的RPC扩展；
3） 在Dubbo的filter机制上， 实现REST权限控制， 可参考dubbox；
4） 实现一个自定义Dubbo的Cluster/Loadbalance扩展， 如果一分钟内调用某个服务/
提供者超过10次， 则拒绝提供服务直到下一分钟；
5） 整合Dubbo+Sentinel， 实现限流功能；
6） 整合Dubbo与Skywalking， 实现全链路性能监控。  

**答题**


**参考**

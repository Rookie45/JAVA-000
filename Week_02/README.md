学习笔记



GC日志解读与分析
======

**串行GC，通过`-XX:+UseSerialGC`参数开启**，通过`-XX:+PrintGCDetails -XX:+PrintGCDateStamps`参数打印带有时间戳的GC日志，通过`-Xloggc:gc.demo.log`参数指定GC日志输出到某个文件内，这里是gc.demo.log文件。

```java
2020-10-26T22:13:54.096+0800: 0.427: [GC (Allocation Failure) 2020-10-26T22:13:54.096+0800: 0.427: [DefNew: 139776K->17472K(157248K), 0.0395084 secs] 139776K->57031K(506816K), 0.0396923 secs] [Times: user=0.01 sys=0.01, real=0.04 secs] 
```

> 上述是使用串行垃圾收集器的一次Young GC，GC发生的时间`2020-10-26T22:13:54.096`，`+0800`为东八时区，`0.427`为GC事件相对于JVM启动的时间间隔，发生的原因是分配内存失败`Allocation Failure`，年轻代`DefNew`（Default New Generation）使用串行收集器回收空间，使用空间从139776KB减少到17472KB，总的年轻代可用内存为157248KB，GC耗时约39.7ms；堆（heap）的内存使用从139776KB减少到57031KB，总的堆可用内存为506816KB，GC耗时约40ms；可以根据上面的堆和新生代的信息，可以得出此处GC，有57031-17472=39559KB的对象晋升老年代，且老年代总的可用空间为506816KB-157248KB=349568KB。后面的时间统计，`user`部分表示所有 GC线程消耗的CPU时间，此处为10ms，`sys`部分表示系统调用和系统等待事件消耗的时间，此处为10ms，`real`表示应用程序GC暂停的时间，此处为40ms。

-  real —— 程序从开始到结束所用的时钟时间。这个时间包括其他进程使用的时间片和进程阻塞的时间（比如等待 I/O 完成）。 
-  user —— 进程执行用户态代码（核心之外）所使用的时间。这是执行此进程所使用的实际 CPU 时间，其他进程和此进程阻塞的时间并不包括在内。在垃圾收集的情况下，表示 GC 线程执行所使用的 CPU 总时间。 
-  sys —— 进程在内核态消耗的 CPU 时间，即在内核执行系统调用或等待系统事件所使用的 CPU 时间。

```java
2020-10-26T08:07:43.116+0800: 0.767: [Full GC (Allocation Failure) 2020-10-28T08:07:43.116+0800: 0.767: [Tenured: 174685K->174708K(174784K), 0.0344185 secs] 253204K->192073K(253440K), [Metaspace: 3291K->3291K(1056768K)], 0.0345011 secs] [Times: user=0.05 sys=0.00, real=0.05 secs] 
```

> 上述



**并行GC，通过`-XX:+UseParallelGC`参数开启**

```
2020-10-28T08:29:49.726+0800: 0.449: [GC (Allocation Failure) [PSYoungGen: 65467K->10740K(76288K)] 65467K->20801K(251392K), 0.0046561 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
```





```
2020-10-26T21:45:53.744+0800: 1.275: [Full GC (Ergonomics) [PSYoungGen: 28811K->0K(116736K)] [ParOldGen: 315346K->272215K(349696K)] 344158K->272215K(466432K), [Metaspace: 4042K->4042K(1056768K)], 0.0664040 secs] [Times: user=0.34 sys=0.00, real=0.07 secs] 
```
Ergonomics翻译成中文，一般都是“人体工程学”。在JVM中的垃圾收集器中的Ergonomics就是负责自动的调解gc暂停时间和吞吐量之间的平衡，然后你的虚拟机性能更好的一种做法。

对于注重吞吐量的收集器来说，在某个generation被过渡使用之前，GC ergonomics就会启动一次GC。

正如我们前面提到的，发生本次full gc正是在使用Parallel Scavenge收集器的情况下发生的。

而Parallel Scavenge正是一款注重吞吐量的收集器：

Parallel Scavenge的目标是达到一个可控的吞吐量，吞吐量=程序运行时间/（程序运行时间+GC时间），如程序运行了99s，GC耗时1s，吞吐量=99/（99+1）=99%。Parallel Scavenge提供了两个参数用以精确控制吞吐量，分别是用以控制最大GC停顿时间的-XX:MaxGCPauseMillis及直接控制吞吐量的参数-XX:GCTimeRatio。







JVM 线程堆栈数据分析  
===

压测工具[**SuperBenchmarker**](https://github.com/aliostad/SuperBenchmarker)


- -c --concurrency (Default: 1) 并发请求数
- -n, --numberOfRequests (Default: 100) 请求数量
- -m, --method (Default: GET) HTTP Method
- -u, --url 请求地址，也就是需要压测的地址
- -v,--verbose：输出详细
- -h, --headers：输出HTTP Header
- -k, --cookies：输出cookie
- -q, --onlyRequest：仅输出请求信息



内存分析与相关工具
====



JVM问题分析调优经验
====



GC疑难情况问题分析
====



Java Socket编程
====



深入讨论IO
====



IO模型与相关概念
====



Netty框架简介与示例
====




参考

[SuperBenchmarker](https://github.com/aliostad/SuperBenchmarker)

[读懂一行Full GC日志](https://cloud.tencent.com/developer/article/1082687)

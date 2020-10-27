学习笔记



GC日志解读与分析
======
```
2020-10-26T21:45:53.744+0800: 1.275: [Full GC (Ergonomics) [PSYoungGen: 28811K->0K(116736K)] [ParOldGen: 315346K->272215K(349696K)] 344158K->272215K(466432K), [Metaspace: 4042K->4042K(1056768K)], 0.0664040 secs] [Times: user=0.34 sys=0.00, real=0.07 secs] 
```
Ergonomics翻译成中文，一般都是“人体工程学”。在JVM中的垃圾收集器中的Ergonomics就是负责自动的调解gc暂停时间和吞吐量之间的平衡，然后你的虚拟机性能更好的一种做法。

对于注重吞吐量的收集器来说，在某个generation被过渡使用之前，GC ergonomics就会启动一次GC。

正如我们前面提到的，发生本次full gc正是在使用Parallel Scavenge收集器的情况下发生的。

而Parallel Scavenge正是一款注重吞吐量的收集器：

Parallel Scavenge的目标是达到一个可控的吞吐量，吞吐量=程序运行时间/（程序运行时间+GC时间），如程序运行了99s，GC耗时1s，吞吐量=99/（99+1）=99%。Parallel Scavenge提供了两个参数用以精确控制吞吐量，分别是用以控制最大GC停顿时间的-XX:MaxGCPauseMillis及直接控制吞吐量的参数-XX:GCTimeRatio。

[Times: user=0.03 sys=0.00, real=0.05 secs] ，与

-  real —— 程序从开始到结束所用的时钟时间。这个时间包括其他进程使用的时间片和进程阻塞的时间（比如等待 I/O 完成）。 
-  user —— 进程执行用户态代码（核心之外）所使用的时间。这是执行此进程所使用的实际 CPU 时间，其他进程和此进程阻塞的时间并不包括在内。在垃圾收集的情况下，表示 GC 线程执行所使用的 CPU 总时间。 
-  sys —— 进程在内核态消耗的 CPU 时间，即在内核执行系统调用或等待系统事件所使用的 CPU 时间。





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





参考

[SuperBenchmarker](https://github.com/aliostad/SuperBenchmarker)

[读懂一行Full GC日志](https://cloud.tencent.com/developer/article/1082687)

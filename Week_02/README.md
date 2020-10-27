学习笔记



GC日志解读与分析
======

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
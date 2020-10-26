**题目1：**

使用 GCLogAnalysis.java 自己演练一遍串行/并行/CMS/G1的案例。   

**答题如下**：

> **串行GC，堆大小512M**

```java
Java HotSpot(TM) 64-Bit Server VM (25.191-b12) for windows-amd64 JRE (1.8.0_191-b12), built on Oct  6 2018 09:29:03 by "java_re" with MS VC++ 10.0 (VS2010)
Memory: 4k page, physical 16611316k(9464840k free), swap 17659892k(9832372k free)
CommandLine flags: -XX:InitialHeapSize=536870912 -XX:MaxHeapSize=536870912 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseSerialGC  
2020-10-25T16:28:30.492+0800: 0.224: [GC (Allocation Failure) 2020-10-25T16:28:30.492+0800: 0.224: [DefNew: 139776K->17472K(157248K), 0.0391739 secs] 139776K->46387K(506816K), 0.0393111 secs] [Times: user=0.03 sys=0.00, real=0.05 secs] 
...
2020-10-25T16:28:31.367+0800: 1.092: [Full GC (Allocation Failure) 2020-10-25T16:28:31.367+0800: 1.092: [Tenured: 349499K->349393K(349568K), 0.0399489 secs] 506730K->372468K(506816K), [Metaspace: 3794K->3794K(1056768K)], 0.0399981 secs] [Times: user=0.03 sys=0.00, real=0.03 secs] 
Heap
 def new generation   total 157248K, used 72766K [0x00000000e0000000, 0x00000000eaaa0000, 0x00000000eaaa0000)
  eden space 139776K,  52% used [0x00000000e0000000, 0x00000000e470fb50, 0x00000000e8880000)
  from space 17472K,   0% used [0x00000000e9990000, 0x00000000e9990000, 0x00000000eaaa0000)
  to   space 17472K,   0% used [0x00000000e8880000, 0x00000000e8880000, 0x00000000e9990000)
 tenured generation   total 349568K, used 349393K [0x00000000eaaa0000, 0x0000000100000000, 0x0000000100000000)
   the space 349568K,  99% used [0x00000000eaaa0000, 0x00000000fffd4600, 0x00000000fffd4600, 0x0000000100000000)
 Metaspace       used 3801K, capacity 4540K, committed 4864K, reserved 1056768K
  class space    used 419K, capacity 428K, committed 512K, reserved 1048576K
```

截取了使用串行垃圾收集器的一次GC和一次Full GC，简单分析：

GC发生的原因是分配内存失败`Allocation Failure`，年轻代使用串行收集器`DefNew`回收（Serial收集器中的新生代名为Default New Generation），使用空间从139776KB减少到17472KB，总的年轻代可用内存为157248KB，GC耗时约39ms；堆（heap）的内存使用从139776KB减少到46387KB，总的可用内存为506816KB，GC耗时39ms；可以根据上面的堆和新生代的信息，可以得出此处GC，有46387-17472=28915KB的对象晋升老年代，且老年代总的可用空间为506816KB-157248KB=349568KB。后面的时间统计，`user`部分表示所有 GC线程消耗的CPU时间，此处为30ms，`sys`部分表示系统调用和系统等待事件消耗的时间（线程上下文切换），此处为0ms，`real`表示应用程序暂停的时间，此处为30ms。

  Full GC发生的原因也是分配内存失败`Allocation Failure`，GC发生的区域为老年代（Tenured），也是使用的串行GC，回收后老年代空间从349499KB减少到349393KB，总的老年代可使用空间349568KB，GC耗时约40ms；堆（heap）的内存使用从506730KB减少到372468KB，总的可用内存为506816KB；metaspace没有发生变化。总的时间统计，GC线程消耗30ms，上线文切换0ms，总的GC执行时间为30ms。



> **并行GC，堆大小512M**



> **CMS GC，堆大小512M**



> **G1 GC，堆大小512M**



> **串行 GC，堆大小1024M**



> **并行GC，堆大小1024M**



> **CMS GC，堆大小1024M**



> **G1 GC，堆大小1024M**



> **串行 GC，堆大小2048M**



> **并行GC，堆大小2048M**



> **CMS GC，堆大小2048M**



> **G1 GC，堆大小2048M**



> **串行 GC，堆大小4086M**



> **并行GC，堆大小4086M**



> **CMS GC，堆大小4086M**



> **G1 GC，堆大小4086M**





**题目2：**

使用压测工具（wrk或sb），演练gateway-server-0.0.1-SNAPSHOT.jar 示例  

**答题如下**：



**题目3：**

如果自己本地有可以运行的项目，可以按照2的方式进行演练  

**答题如下**：



**题目4：**

运行课上的例子，以及 Netty 的例子，分析相关现象。  

**答题如下**：



**题目5：**

写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801，代码提交到
Github  

**答题如下：**



参考

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)

[如何选择合适的 GC 时间 —— USER, SYS, REAL？](https://cloud.tencent.com/developer/article/1491229)


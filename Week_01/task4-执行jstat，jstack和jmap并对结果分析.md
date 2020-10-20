**题目**

检查一下自己维护的业务系统的 JVM 参数配置，用 jstat 和 jstack、jmap 查看一下详情，并且自己独立分析一下大概情况，思考有没有不合理的地方，如何改进。

**答题如下：**

> **jps -mlv**

```java
...
8188 ltd.newbee.mall.NewBeeMallApplication 
-XX:TieredStopAtLevel=1 
-Xverify:none
-Dspring.output.ansi.enabled=always 
-Dcom.sun.management.jmxremote 
-Dcom.sun.management.jmxremote.port=56857 
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false 
-Djava.rmi.server.hostname=localhost 
-Dspring.liveBeansView.mbeanDomain  #不关心
-Dspring.application.admin.enabled=true 
-javaagent:D:\Program Files\JetBrains\IntelliJ IDEA 2018.3.2\lib\idea_rt.jar=56860:D:\Program Files\JetBrains\IntelliJ IDEA 2018.3.2\bin 
-Dfile.encoding=UTF-8
```

> -XX:TieredStopAtLevel=1 表示解释执行后，直接由C1编译器进行编译。在java8开始，默认开启分层编译，原来的参数-client和-server参数无效。C1是Hotspot虚拟机中的一个即时编译器。
>
> -Xverify:none 表示禁用验证器，该值在java13中被废弃，与此相对的，-Xverify:all 表示对所有类启动验证，另外还有-Xverify和-Xverify:remote，两者等效，表示对所有非引导类启动验证。
>
> -Dcom.sun.management.jmxremote 表示是否支持远程JMX访问，默认true 
>
> -Dcom.sun.management.jmxremote.port=56857 表示监听端口号56857，方便远程访问 
>
> -Dcom.sun.management.jmxremote.authenticate=false 表示不开启用户认证，默认开启
>
> -Dcom.sun.management.jmxremote.ssl=false 表示连接不开启SSL加密，默认开启 
>
> -Djava.rmi.server.hostname=localhost 表示主机名字符串，用于给客户端调用自己对象上的方法，默认本地主机IP地址
>
> -Dspring.application.admin.enabled=true 表示开启与管理员相关的功能,可以用于远程管理springboot应用
>
> -javaagent:jarpath[=options]启动外部的agent库
>
> -Dfile.encoding=UTF-8 设置文件编码UTF-8



> **jstat -gc 8188 1000 10**

```java
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU   CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
...
15360.0 16896.0  0.0    0.0   203264.0 202256.3  74752.0    19413.4   35496.0 33775.6 4864.0 4561.2      8    0.446   2      0.327    0.773
15360.0 16896.0  0.0    0.0   203264.0 203264.0  74752.0    19413.4   35496.0 33775.6 4864.0 4561.2      8    0.446   2      0.327    0.773
19456.0 16896.0  0.0   16878.4 203264.0  9028.1   74752.0    21034.3   49664.0 47024.7 6656.0 6163.1      9    0.531   2      0.327    0.857
19456.0 16896.0  0.0   16878.4 203264.0 10072.2   74752.0    21034.3   49664.0 47024.7 6656.0 6163.1      9    0.531   2      0.327    0.857
...
```
> S0C：表示s0存活区的当前容量，单位kB（后面容量均为kB）；S1C：表示s1存活区的当前容量；S0U表示s0存活区的使用量；S1U：表示s1存活区的使用量；EC：表示Eden区新生代的当前容量；EU：表示Eden区新生代的使用量； OC：表示Old区老年代当前容量； OU：表示Old区老年代使用量；MC：表示元数据区的容量； MU：表示元数据区的使用量；CCSC：压缩的class空间容量；CCSU：压缩的class空间使用量； YGC：年轻代GC的次数；YGCT：年轻代GC消耗总时间，单位ms（后面时间均为ms）；FGC：Full GC的次数；FGCT：Full GC消耗总时间； GCT：垃圾收集消耗的总时间；
>
> 据此分析运行的web程序，中间发生了一次Young GC，耗时0.085ms，Eden区使用量从203264.0kB降到9028.1kB，S1使用量从0.0kB涨到16878.4kB。



> **jmap -heap 8188**

```java
Attaching to process ID 8188, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.112-b15

using thread-local object allocation.
Parallel GC with 8 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 0//对应jvm启动参数-XX:MinHeapFreeRatio，设置JVM堆最小空闲比率(default 40)
   MaxHeapFreeRatio         = 100//对应jvm启动参数 -XX:MaxHeapFreeRatio，设置JVM堆最大空闲比率(default 70)
   MaxHeapSize              = 2116026368 (2018.0MB)//对应jvm启动参数-XX:MaxHeapSize，设置JVM堆的最大大小
   NewSize                  = 44564480 (42.5MB)//对应jvm启动参数-XX:NewSize，设置JVM堆的‘新生代’的默认大小
   MaxNewSize               = 705167360 (672.5MB)//对应jvm启动参数-XX:MaxNewSize，设置JVM堆的‘新生代’的最大大小
   OldSize                  = 89653248 (85.5MB)//对应jvm启动参数-XX:OldSize，设置JVM堆的‘老生代’的大小
   NewRatio                 = 2//对应jvm启动参数-XX:NewRatio，设置‘新生代’和‘老生代’的大小比率
   SurvivorRatio            = 8//对应jvm启动参数-XX:SurvivorRatio，设置年轻代中Eden区与Survivor区的大小比值 
   MetaspaceSize            = 21807104 (20.796875MB)//对应jvm启动参数-XX:MetaspaceSize，设置元空间的初始大小
   CompressedClassSpaceSize = 1073741824 (1024.0MB)//
   MaxMetaspaceSize         = 17592186044415 MB//对应jvm启动参数-XX:MaxMetaspaceSize，设置元空间最大值，默认-1，即不限制
   G1HeapRegionSize         = 0 (0.0MB)//对应jvm启动参数-XX:G1HeapRegionSize，设置G1中每个region的大小取值范围1MB-32MB，且为2的N次幂

Heap Usage:
PS Young Generation
Eden Space:
   capacity = 154140672 (147.0MB)//Eden区容量
   used     = 53923488 (51.425445556640625MB) //Eden区已使用
   free     = 100217184 (95.57455444335938MB) //Eden区剩余容量
   34.983296297034435% used
From Space://Survivor区的内存分布
   capacity = 15728640 (15.0MB)
   used     = 0 (0.0MB)
   free     = 15728640 (15.0MB)
   0.0% used
To Space://另一个Survivor区的内存分布
   capacity = 17301504 (16.5MB)
   used     = 0 (0.0MB)
   free     = 17301504 (16.5MB)
   0.0% used
PS Old Generation //old区内存分布
   capacity = 94896128 (90.5MB)
   used     = 20052584 (19.123634338378906MB)
   free     = 74843544 (71.3763656616211MB)
   21.131087666717022% used

17988 interned Strings occupying 2308376 bytes.
```

> 依据输出进行分析，首先采用并行GC策略，GC使用的线程数为8，最大新生代的堆大小672.5MB，那么最大老年代的堆大小2018.0 - 672.5=1345.5，1345.5/672.5 = 2 ，恰好为NewRatio的值。另外根据NewSize，MaxNewSize和SurvivorRatio，可以计算Eden区的容量区间在[34, 538]，Survivor区的容量区间在[4.25, 67.25]。观察上面的数值，会发现两个Survivor大小不一样，另外Eden区/Survivor区并不是8，这是因为在没有指定严格执行比例时，jvm允许容量区间范围内变化，可以通过-XX:-UseAdaptiveSizePolicy -XX:SurvivorRatio=8这两个参数，让E:S0:S1三者大小严格8:1:1，s0和s1一样大。
> 



> **jstack -l 14512**

```java
2020-10-19 14:12:11
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.191-b12 mixed mode):

"Thread-1" #13 prio=5 os_prio=0 tid=0x000000001f0a9800 nid=0x26bc waiting for monitor entry [0x000000001febf000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at com.sl.java00.week01.DeadLock$2.run(DeadLock.java:41)
	- waiting to lock <0x000000076b9a5268> (a java.lang.Object)
	- locked <0x000000076b9a5278> (a java.lang.Object)

   Locked ownable synchronizers:
	- None

"Thread-0" #12 prio=5 os_prio=0 tid=0x000000001f0a5000 nid=0x5744 waiting for monitor entry [0x000000001fdbf000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at com.sl.java00.week01.DeadLock$1.run(DeadLock.java:23)
	- waiting to lock <0x000000076b9a5278> (a java.lang.Object)
	- locked <0x000000076b9a5268> (a java.lang.Object)

   Locked ownable synchronizers:
	- None

"Service Thread" #11 daemon prio=9 os_prio=0 tid=0x000000001ef79800 nid=0x41d4 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"C1 CompilerThread3" #10 daemon prio=9 os_prio=2 tid=0x000000001ef5e000 nid=0x648 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"C2 CompilerThread2" #9 daemon prio=9 os_prio=2 tid=0x000000001ef49800 nid=0x5a6c waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"C2 CompilerThread1" #8 daemon prio=9 os_prio=2 tid=0x000000001ef45800 nid=0x1fa8 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"C2 CompilerThread0" #7 daemon prio=9 os_prio=2 tid=0x000000001ef2b800 nid=0x4ec4 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"Monitor Ctrl-Break" #6 daemon prio=5 os_prio=0 tid=0x000000001ef2a000 nid=0x451c runnable [0x000000001f6be000]
   java.lang.Thread.State: RUNNABLE
	at java.net.SocketInputStream.socketRead0(Native Method)
	at java.net.SocketInputStream.socketRead(SocketInputStream.java:116)
	at java.net.SocketInputStream.read(SocketInputStream.java:171)
	at java.net.SocketInputStream.read(SocketInputStream.java:141)
	at sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:284)
	at sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:326)
	at sun.nio.cs.StreamDecoder.read(StreamDecoder.java:178)
	- locked <0x000000076bace818> (a java.io.InputStreamReader)
	at java.io.InputStreamReader.read(InputStreamReader.java:184)
	at java.io.BufferedReader.fill(BufferedReader.java:161)
	at java.io.BufferedReader.readLine(BufferedReader.java:324)
	- locked <0x000000076bace818> (a java.io.InputStreamReader)
	at java.io.BufferedReader.readLine(BufferedReader.java:389)
	at com.intellij.rt.execution.application.AppMainV2$1.run(AppMainV2.java:64)

   Locked ownable synchronizers:
	- None

"Attach Listener" #5 daemon prio=5 os_prio=2 tid=0x000000001ee51000 nid=0x20cc waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"Signal Dispatcher" #4 daemon prio=9 os_prio=2 tid=0x000000001edf9800 nid=0x1cb4 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"Finalizer" #3 daemon prio=8 os_prio=1 tid=0x000000001cfde000 nid=0x4504 in Object.wait() [0x000000001f3bf000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x000000076b808ed0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)
	- locked <0x000000076b808ed0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)
	at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:216)

   Locked ownable synchronizers:
	- None

"Reference Handler" #2 daemon prio=10 os_prio=2 tid=0x000000001ede0800 nid=0x37b4 in Object.wait() [0x000000001f2bf000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x000000076b806bf8> (a java.lang.ref.Reference$Lock)
	at java.lang.Object.wait(Object.java:502)
	at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
	- locked <0x000000076b806bf8> (a java.lang.ref.Reference$Lock)
	at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

   Locked ownable synchronizers:
	- None

"main" #1 prio=5 os_prio=0 tid=0x0000000003543800 nid=0x162c waiting on condition [0x0000000002f3e000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x000000076b9aaf38> (a java.util.concurrent.CountDownLatch$Sync)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.doAcquireSharedInterruptibly(AbstractQueuedSynchronizer.java:997)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireSharedInterruptibly(AbstractQueuedSynchronizer.java:1304)
	at java.util.concurrent.CountDownLatch.await(CountDownLatch.java:231)
	at com.sl.java00.week01.DeadLock.main(DeadLock.java:48)

   Locked ownable synchronizers:
	- None

"VM Thread" os_prio=2 tid=0x000000001cfda000 nid=0x3600 runnable 

"GC task thread#0 (ParallelGC)" os_prio=0 tid=0x0000000003559800 nid=0x147c runnable 

"GC task thread#1 (ParallelGC)" os_prio=0 tid=0x000000000355b000 nid=0x124c runnable 

"GC task thread#2 (ParallelGC)" os_prio=0 tid=0x000000000355c800 nid=0x4d70 runnable 

"GC task thread#3 (ParallelGC)" os_prio=0 tid=0x000000000355e000 nid=0x34c0 runnable 

"GC task thread#4 (ParallelGC)" os_prio=0 tid=0x0000000003561000 nid=0x175c runnable 

"GC task thread#5 (ParallelGC)" os_prio=0 tid=0x0000000003562800 nid=0x577c runnable 

"GC task thread#6 (ParallelGC)" os_prio=0 tid=0x0000000003565800 nid=0x57ac runnable 

"GC task thread#7 (ParallelGC)" os_prio=0 tid=0x0000000003566800 nid=0x438 runnable 

"VM Periodic Task Thread" os_prio=2 tid=0x000000001f09b800 nid=0x72c waiting on condition 

JNI global references: 12


Found one Java-level deadlock:
=============================
"Thread-1":
  waiting to lock monitor 0x000000001f0b27b8 (object 0x000000076b9a5268, a java.lang.Object),
  which is held by "Thread-0"
"Thread-0":
  waiting to lock monitor 0x000000000363eab8 (object 0x000000076b9a5278, a java.lang.Object),
  which is held by "Thread-1"

Java stack information for the threads listed above:
===================================================
"Thread-1":
	at com.sl.java00.week01.DeadLock$2.run(DeadLock.java:41)
	- waiting to lock <0x000000076b9a5268> (a java.lang.Object)
	- locked <0x000000076b9a5278> (a java.lang.Object)
"Thread-0":
	at com.sl.java00.week01.DeadLock$1.run(DeadLock.java:23)
	- waiting to lock <0x000000076b9a5278> (a java.lang.Object)
	- locked <0x000000076b9a5268> (a java.lang.Object)

Found 1 deadlock.
```

> 据上述输出数据进行分析，jstack用于查看线程堆栈信息，在发生死锁的时候可以利用这个命令查找死锁或者在发生死循环的时候利用此命令排查。观察输出的最后的结果**Found 1 deadlock**，发现一个死锁，往上看，DeadLock类的Thread1在持有<0x000000076b9a5278>，等待<0x000000076b9a5268>，而DeadLock类的Thread0在持有<0x000000076b9a5268>，等待<0x000000076b9a5278>，由此形成了循环等待，导致死锁。从上面的信息，可以清楚看到发生问题的类DeadLock，发生问题的代码行数41和23，争抢的锁资源为两个Object对象。
>
> 上述信息中几个参数的含义：
>
>tid：java内的线程ID；nid：操作系统级别的线程ID；prio：java内定义的线程优先级；os_prio：操作系统级别的优先级
>
> 一般jstack输出信息比较长，控制台输出阅读不便，所以会结合“>”输出到文本中，比如`jstack -l 14512 > stack14512.txt`。另外，循环死锁一般配合Top命令一起使用，因为循环死锁会导致CPU不断升高，可以通过`top`命令查看占用CPU高的进程，再通过`top -H -p PID`查看进程中具体哪条线程CPU利用率高，由于在top中使用的是10进制，在jstack中打印的线程是16进制，要对应上线程id需要做个转换。



参考

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)

[Java的即时编译器](https://www.jianshu.com/p/5df32841da93)

[xverify](https://www.eclipse.org/openj9/docs/xverify/)

[JMX学习笔记](https://www.jianshu.com/p/414647c1179e)

[Java命令: Jmap](https://www.jianshu.com/p/c52ffaca40a5)

[Java命令行监控工具(jmap,jstack,jstat,jinfo,jps)](https://yq.aliyun.com/articles/666966)

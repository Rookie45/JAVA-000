jps -mlv

```java
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

-XX:TieredStopAtLevel=1 表示解释执行后，直接由C1编译器进行编译。在java8开始，默认开启分层编译，原来的参数-client和-server参数无效。C1是Hotspot虚拟机中的一个即时编译器。
-Xverify:none 表示禁用验证器，该值在java13中被废弃，与此相对的，-Xverify:all 表示对所有类启动验证，另外还有-Xverify和-Xverify:remote，两者等效，表示对所有非引导类启动验证。
-Dcom.sun.management.jmxremote 表示是否支持远程JMX访问，默认true 
-Dcom.sun.management.jmxremote.port=56857 表示监听端口号56857，方便远程访问 
-Dcom.sun.management.jmxremote.authenticate=false 表示不开启用户认证，默认开启
-Dcom.sun.management.jmxremote.ssl=false 表示连接不开启SSL加密，默认开启 
-Djava.rmi.server.hostname=localhost 表示主机名字符串，用于给客户端调用自己对象上的方法，默认本地主机IP地址
-Dspring.application.admin.enabled=true 表示开启与管理员相关的功能,可以用于远程管理springboot应用
-javaagent:jarpath[=options]启动外部的agent库
-Dfile.encoding=UTF-8 设置文件编码UTF-8

===========================
jstat -gc 8188 1000 10

 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU   CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
...
15360.0 16896.0  0.0    0.0   203264.0 202256.3  74752.0    19413.4   35496.0 33775.6 4864.0 4561.2      8    0.446   2      0.327    0.773
15360.0 16896.0  0.0    0.0   203264.0 203264.0  74752.0    19413.4   35496.0 33775.6 4864.0 4561.2      8    0.446   2      0.327    0.773
19456.0 16896.0  0.0   16878.4 203264.0  9028.1   74752.0    21034.3   49664.0 47024.7 6656.0 6163.1      9    0.531   2      0.327    0.857
19456.0 16896.0  0.0   16878.4 203264.0 10072.2   74752.0    21034.3   49664.0 47024.7 6656.0 6163.1      9    0.531   2      0.327    0.857
...

S0C：表示s0存活区的当前容量，单位kB（后面容量均为kB）；S1C：表示s1存活区的当前容量；S0U表示s0存活区的使用量；S1U：表示s1存活区的使用量；EC：表示Eden区新生代的当前容量；EU：表示Eden区新生代的使用量； OC：表示Old区老年代当前容量； OU：表示Old区老年代使用量；MC：表示元数据区的容量； MU：表示元数据区的使用量；CCSC：压缩的class空间容量；CCSU：压缩的class空间使用量； YGC：年轻代GC的次数；YGCT：年轻代GC消耗总时间，单位ms（后面时间均为ms）；FGC：Full GC的次数；FGCT：Full GC消耗总时间； GCT：垃圾收集消耗的总时间；
据此分析运行的web程序，中间发生了一次Young GC，耗时0.085ms

jmap -heap 8188

Attaching to process ID 8188, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.112-b15

using thread-local object allocation.
Parallel GC with 8 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 0
   MaxHeapFreeRatio         = 100
   MaxHeapSize              = 2116026368 (2018.0MB)
   NewSize                  = 44564480 (42.5MB)
   MaxNewSize               = 705167360 (672.5MB)
   OldSize                  = 89653248 (85.5MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
PS Young Generation
Eden Space:
   capacity = 154140672 (147.0MB)
   used     = 53923488 (51.425445556640625MB)
   free     = 100217184 (95.57455444335938MB)
   34.983296297034435% used
From Space:
   capacity = 15728640 (15.0MB)
   used     = 0 (0.0MB)
   free     = 15728640 (15.0MB)
   0.0% used
To Space:
   capacity = 17301504 (16.5MB)
   used     = 0 (0.0MB)
   free     = 17301504 (16.5MB)
   0.0% used
PS Old Generation
   capacity = 94896128 (90.5MB)
   used     = 20052584 (19.123634338378906MB)
   free     = 74843544 (71.3763656616211MB)
   21.131087666717022% used

17988 interned Strings occupying 2308376 bytes.

jstack -l 8188

2020-10-19 14:12:11
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.112-b15 mixed mode):

"DestroyJavaVM" #61 prio=5 os_prio=0 tid=0x000000005d3cb800 nid=0x2524 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"http-nio-28089-AsyncTimeout" #59 daemon prio=5 os_prio=0 tid=0x000000005d3cb000 nid=0x2794 waiting on condition [0x00000000605ef000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at org.apache.coyote.AbstractProtocol$AsyncTimeout.run(AbstractProtocol.java:1113)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"http-nio-28089-Acceptor-0" #58 daemon prio=5 os_prio=0 tid=0x000000005d3ca000 nid=0x2e44 runnable [0x00000000604de000]
   java.lang.Thread.State: RUNNABLE
	at sun.nio.ch.ServerSocketChannelImpl.accept0(Native Method)
	at sun.nio.ch.ServerSocketChannelImpl.accept(ServerSocketChannelImpl.java:422)
	at sun.nio.ch.ServerSocketChannelImpl.accept(ServerSocketChannelImpl.java:250)
	- locked <0x00000000d82a2258> (a java.lang.Object)
	at org.apache.tomcat.util.net.NioEndpoint.serverSocketAccept(NioEndpoint.java:446)
	at org.apache.tomcat.util.net.NioEndpoint.serverSocketAccept(NioEndpoint.java:70)
	at org.apache.tomcat.util.net.Acceptor.run(Acceptor.java:95)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"http-nio-28089-ClientPoller-1" #57 daemon prio=5 os_prio=0 tid=0x000000005d3c9800 nid=0x3314 runnable [0x00000000603ce000]
   java.lang.Thread.State: RUNNABLE
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.poll0(Native Method)
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.poll(WindowsSelectorImpl.java:296)
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.access$400(WindowsSelectorImpl.java:278)
	at sun.nio.ch.WindowsSelectorImpl.doSelect(WindowsSelectorImpl.java:159)
	at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
	- locked <0x00000000d8421ba0> (a sun.nio.ch.Util$3)
	- locked <0x00000000d8421b90> (a java.util.Collections$UnmodifiableSet)
	- locked <0x00000000d8421a40> (a sun.nio.ch.WindowsSelectorImpl)
	at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
	at org.apache.tomcat.util.net.NioEndpoint$Poller.run(NioEndpoint.java:742)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"http-nio-28089-ClientPoller-0" #56 daemon prio=5 os_prio=0 tid=0x000000005d3c8800 nid=0x1140 runnable [0x000000006023e000]
   java.lang.Thread.State: RUNNABLE
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.poll0(Native Method)
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.poll(WindowsSelectorImpl.java:296)
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.access$400(WindowsSelectorImpl.java:278)
	at sun.nio.ch.WindowsSelectorImpl.doSelect(WindowsSelectorImpl.java:159)
	at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
	- locked <0x00000000d841d948> (a sun.nio.ch.Util$3)
	- locked <0x00000000d841d938> (a java.util.Collections$UnmodifiableSet)
	- locked <0x00000000d841d7e8> (a sun.nio.ch.WindowsSelectorImpl)
	at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
	at org.apache.tomcat.util.net.NioEndpoint$Poller.run(NioEndpoint.java:742)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"http-nio-28089-exec-10" #55 daemon prio=5 os_prio=0 tid=0x000000005d3c8000 nid=0x2c8c waiting on condition [0x000000006011e000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000d82faff8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"http-nio-28089-exec-9" #54 daemon prio=5 os_prio=0 tid=0x000000005d3c7000 nid=0x1158 waiting on condition [0x000000005ff8e000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000d82faff8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"http-nio-28089-exec-8" #53 daemon prio=5 os_prio=0 tid=0x000000005d3c6800 nid=0x3044 waiting on condition [0x000000005fe1f000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000d82faff8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"http-nio-28089-exec-7" #52 daemon prio=5 os_prio=0 tid=0x000000005d3c5800 nid=0x35f0 waiting on condition [0x000000005fbde000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000d82faff8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"http-nio-28089-exec-6" #51 daemon prio=5 os_prio=0 tid=0x000000005d3c5000 nid=0x278c waiting on condition [0x000000005fadf000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000d82faff8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"http-nio-28089-exec-5" #50 daemon prio=5 os_prio=0 tid=0x000000005d3c4000 nid=0x34dc waiting on condition [0x000000005ecce000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000d82faff8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"http-nio-28089-exec-4" #49 daemon prio=5 os_prio=0 tid=0x000000005d3c3800 nid=0x37d8 waiting on condition [0x000000005f66e000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000d82faff8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"http-nio-28089-exec-3" #48 daemon prio=5 os_prio=0 tid=0x000000005d3c2800 nid=0x3340 waiting on condition [0x000000005f8fe000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000d82faff8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"http-nio-28089-exec-2" #47 daemon prio=5 os_prio=0 tid=0x000000005d3c2000 nid=0x29e0 waiting on condition [0x000000005f76e000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000d82faff8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"http-nio-28089-exec-1" #46 daemon prio=5 os_prio=0 tid=0x000000005d3c1000 nid=0x2908 waiting on condition [0x000000005f53f000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000d82faff8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"NioBlockingSelector.BlockPoller-1" #45 daemon prio=5 os_prio=0 tid=0x000000005d3c0800 nid=0x2848 runnable [0x000000005f34e000]
   java.lang.Thread.State: RUNNABLE
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.poll0(Native Method)
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.poll(WindowsSelectorImpl.java:296)
	at sun.nio.ch.WindowsSelectorImpl$SubSelector.access$400(WindowsSelectorImpl.java:278)
	at sun.nio.ch.WindowsSelectorImpl.doSelect(WindowsSelectorImpl.java:159)
	at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
	- locked <0x00000000d82ad998> (a sun.nio.ch.Util$3)
	- locked <0x00000000d82ad100> (a java.util.Collections$UnmodifiableSet)
	- locked <0x00000000d82acf48> (a sun.nio.ch.WindowsSelectorImpl)
	at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
	at org.apache.tomcat.util.net.NioBlockingSelector$BlockPoller.run(NioBlockingSelector.java:298)

   Locked ownable synchronizers:
	- None

"Abandoned connection cleanup thread" #39 daemon prio=5 os_prio=0 tid=0x000000005d348000 nid=0xb68 in Object.wait() [0x000000005bfdf000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
	- locked <0x0000000082a04128> (a java.lang.ref.ReferenceQueue$Lock)
	at com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.run(AbandonedConnectionCleanupThread.java:70)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- <0x0000000082598360> (a java.util.concurrent.ThreadPoolExecutor$Worker)

"container-0" #38 prio=5 os_prio=0 tid=0x000000005d305800 nid=0xcec waiting on condition [0x000000005e8ff000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at org.apache.catalina.core.StandardServer.await(StandardServer.java:408)
	at org.springframework.boot.web.embedded.tomcat.TomcatWebServer$1.run(TomcatWebServer.java:181)

   Locked ownable synchronizers:
	- None

"ContainerBackgroundProcessor[StandardEngine[Tomcat]]" #37 daemon prio=5 os_prio=0 tid=0x000000005d309800 nid=0x1bd8 waiting on condition [0x000000005edff000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at org.apache.catalina.core.ContainerBase$ContainerBackgroundProcessor.run(ContainerBase.java:1365)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"RMI Scheduler(0)" #17 daemon prio=5 os_prio=0 tid=0x000000005aeff800 nid=0xb40 waiting on condition [0x000000005c44e000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x0000000081f52e58> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:1081)
	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:809)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"RMI TCP Accept-0" #15 daemon prio=5 os_prio=0 tid=0x000000005aceb000 nid=0x363c runnable [0x000000005bd3f000]
   java.lang.Thread.State: RUNNABLE
	at java.net.DualStackPlainSocketImpl.accept0(Native Method)
	at java.net.DualStackPlainSocketImpl.socketAccept(DualStackPlainSocketImpl.java:131)
	at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:409)
	at java.net.PlainSocketImpl.accept(PlainSocketImpl.java:199)
	- locked <0x0000000081f53218> (a java.net.SocksSocketImpl)
	at java.net.ServerSocket.implAccept(ServerSocket.java:545)
	at java.net.ServerSocket.accept(ServerSocket.java:513)
	at sun.management.jmxremote.LocalRMIServerSocketFactory$1.accept(LocalRMIServerSocketFactory.java:52)
	at sun.rmi.transport.tcp.TCPTransport$AcceptLoop.executeAcceptLoop(TCPTransport.java:400)
	at sun.rmi.transport.tcp.TCPTransport$AcceptLoop.run(TCPTransport.java:372)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"RMI TCP Accept-60783" #14 daemon prio=5 os_prio=0 tid=0x000000005ace2800 nid=0x314c runnable [0x000000005be3f000]
   java.lang.Thread.State: RUNNABLE
	at java.net.DualStackPlainSocketImpl.accept0(Native Method)
	at java.net.DualStackPlainSocketImpl.socketAccept(DualStackPlainSocketImpl.java:131)
	at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:409)
	at java.net.PlainSocketImpl.accept(PlainSocketImpl.java:199)
	- locked <0x0000000081f536d8> (a java.net.SocksSocketImpl)
	at java.net.ServerSocket.implAccept(ServerSocket.java:545)
	at java.net.ServerSocket.accept(ServerSocket.java:513)
	at sun.rmi.transport.tcp.TCPTransport$AcceptLoop.executeAcceptLoop(TCPTransport.java:400)
	at sun.rmi.transport.tcp.TCPTransport$AcceptLoop.run(TCPTransport.java:372)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"RMI TCP Accept-0" #13 daemon prio=5 os_prio=0 tid=0x000000005ac97800 nid=0x2234 runnable [0x000000005bbee000]
   java.lang.Thread.State: RUNNABLE
	at java.net.DualStackPlainSocketImpl.accept0(Native Method)
	at java.net.DualStackPlainSocketImpl.socketAccept(DualStackPlainSocketImpl.java:131)
	at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:409)
	at java.net.PlainSocketImpl.accept(PlainSocketImpl.java:199)
	- locked <0x0000000081f28730> (a java.net.SocksSocketImpl)
	at java.net.ServerSocket.implAccept(ServerSocket.java:545)
	at java.net.ServerSocket.accept(ServerSocket.java:513)
	at sun.rmi.transport.tcp.TCPTransport$AcceptLoop.executeAcceptLoop(TCPTransport.java:400)
	at sun.rmi.transport.tcp.TCPTransport$AcceptLoop.run(TCPTransport.java:372)
	at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
	- None

"Service Thread" #11 daemon prio=9 os_prio=0 tid=0x000000005ab40800 nid=0x2688 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"C1 CompilerThread3" #10 daemon prio=9 os_prio=2 tid=0x0000000059173800 nid=0x3624 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"C2 CompilerThread2" #9 daemon prio=9 os_prio=2 tid=0x0000000059172800 nid=0x2e68 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"C2 CompilerThread1" #8 daemon prio=9 os_prio=2 tid=0x0000000059171800 nid=0x37f8 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"C2 CompilerThread0" #7 daemon prio=9 os_prio=2 tid=0x000000005916c800 nid=0x2714 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"Monitor Ctrl-Break" #6 daemon prio=5 os_prio=0 tid=0x000000005916a000 nid=0x312c runnable [0x000000005a6fe000]
   java.lang.Thread.State: RUNNABLE
	at java.net.SocketInputStream.socketRead0(Native Method)
	at java.net.SocketInputStream.socketRead(SocketInputStream.java:116)
	at java.net.SocketInputStream.read(SocketInputStream.java:170)
	at java.net.SocketInputStream.read(SocketInputStream.java:141)
	at sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:284)
	at sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:326)
	at sun.nio.cs.StreamDecoder.read(StreamDecoder.java:178)
	- locked <0x0000000081fa4528> (a java.io.InputStreamReader)
	at java.io.InputStreamReader.read(InputStreamReader.java:184)
	at java.io.BufferedReader.fill(BufferedReader.java:161)
	at java.io.BufferedReader.readLine(BufferedReader.java:324)
	- locked <0x0000000081fa4528> (a java.io.InputStreamReader)
	at java.io.BufferedReader.readLine(BufferedReader.java:389)
	at com.intellij.rt.execution.application.AppMainV2$1.run(AppMainV2.java:64)

   Locked ownable synchronizers:
	- None

"Attach Listener" #5 daemon prio=5 os_prio=2 tid=0x0000000058e87800 nid=0x246c waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"Signal Dispatcher" #4 daemon prio=9 os_prio=2 tid=0x0000000058e9f000 nid=0x1bd0 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"Finalizer" #3 daemon prio=8 os_prio=1 tid=0x0000000057cb2000 nid=0x1628 in Object.wait() [0x000000005a1df000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x0000000081f8cab8> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
	- locked <0x0000000081f8cab8> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:164)
	at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:209)

   Locked ownable synchronizers:
	- None

"Reference Handler" #2 daemon prio=10 os_prio=2 tid=0x0000000057caa000 nid=0x2ea0 in Object.wait() [0x000000005a05e000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x0000000081e3d5a0> (a java.lang.ref.Reference$Lock)
	at java.lang.Object.wait(Object.java:502)
	at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
	- locked <0x0000000081e3d5a0> (a java.lang.ref.Reference$Lock)
	at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

   Locked ownable synchronizers:

	- None

"VM Thread" os_prio=2 tid=0x0000000058e23000 nid=0x2d20 runnable 

"GC task thread#0 (ParallelGC)" os_prio=0 tid=0x0000000002660800 nid=0x1f38 runnable 

"GC task thread#1 (ParallelGC)" os_prio=0 tid=0x0000000002662000 nid=0x3020 runnable 

"GC task thread#2 (ParallelGC)" os_prio=0 tid=0x0000000002663800 nid=0x1acc runnable 

"GC task thread#3 (ParallelGC)" os_prio=0 tid=0x0000000002665000 nid=0x1b50 runnable 

"GC task thread#4 (ParallelGC)" os_prio=0 tid=0x0000000002668800 nid=0x3758 runnable 

"GC task thread#5 (ParallelGC)" os_prio=0 tid=0x0000000002669800 nid=0x7f4 runnable 

"GC task thread#6 (ParallelGC)" os_prio=0 tid=0x000000000266c800 nid=0x211c runnable 

"GC task thread#7 (ParallelGC)" os_prio=0 tid=0x000000000266e000 nid=0x1a1c runnable 

"VM Periodic Task Thread" os_prio=2 tid=0x000000005acf4800 nid=0x2758 waiting on condition 

JNI global references: 1119



参考

[Java的即时编译器](https://www.jianshu.com/p/5df32841da93)

[xverify](https://www.eclipse.org/openj9/docs/xverify/)

[JMX学习笔记](https://www.jianshu.com/p/414647c1179e)

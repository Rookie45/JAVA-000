**题目**

本机使用G1 GC启动一个程序，仿照课上案例分析一下JVM情况

**答题如下**

在IDEA的VM Options添加-XX:+UseG1GC -XX:MaxGCPauseMillis=50，运行程序

```shell
C:\Users\sl>jps -mlv
...
10808 ltd.newbee.mall.NewBeeMallApplication 
-XX:+UseG1GC 
-XX:MaxGCPauseMillis=50
-XX:TieredStopAtLevel=1 
-Xverify:none 
-Dspring.output.ansi.enabled=always 
-Dcom.sun.management.jmxremote 
-Dcom.sun.management.jmxremote.port=60544 
-Dcom.sun.management.jmxremote.authenticate=false 
-Dcom.sun.management.jmxremote.ssl=false 
-Djava.rmi.server.hostname=localhost 
-Dspring.liveBeansView.mbeanDomain 
-Dspring.application.admin.enabled=true 
-javaagent:D:\Program Files\JetBrains\IntelliJ IDEA 2018.3.2\lib\idea_rt.jar=60545:D:\Program Files\JetBrains\IntelliJ IDEA 2018.3.2\bin 
-Dfile.encoding=UTF-8
```

> -XX:+UseG1GC开启G1 GC，-XX:MaxGCPauseMillis=50设置最大垃圾收集停顿时间，单位毫秒。这里注意，垃圾收集停顿时间缩短，是以牺牲吞吐量和新生代空间为代价。

```shell
C:\Users\sl>jmap -heap 10808
Attaching to process ID 10808, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.112-b15

using thread-local object allocation.
Garbage-First (G1) GC with 8 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 40
   MaxHeapFreeRatio         = 70
   MaxHeapSize              = 2116026368 (2018.0MB)
   NewSize                  = 1363144 (1.2999954223632812MB)
   MaxNewSize               = 1268776960 (1210.0MB)
   OldSize                  = 5452592 (5.1999969482421875MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 1048576 (1.0MB)

Heap Usage:
G1 Heap:
   regions  = 2018
   capacity = 2116026368 (2018.0MB)
   used     = 67936560 (64.78935241699219MB)
   free     = 2048089808 (1953.2106475830078MB)
   3.210572468631922% used
G1 Young Generation:
Eden Space:
   regions  = 39
   capacity = 75497472 (72.0MB)
   used     = 40894464 (39.0MB)
   free     = 34603008 (33.0MB)
   54.166666666666664% used
Survivor Space:
   regions  = 8
   capacity = 8388608 (8.0MB)
   used     = 8388608 (8.0MB)
   free     = 0 (0.0MB)
   100.0% used
G1 Old Generation:
   regions  = 18
   capacity = 50331648 (48.0MB)
   used     = 17604912 (16.789352416992188MB)
   free     = 32726736 (31.210647583007812MB)
   34.97781753540039% used

18012 interned Strings occupying 2309984 bytes.
```

> 可以看到开启了G1，同样使用了8个线程执行GC，这个数值可以通过-XX:ParallelGCThreads设置，当CPU核数<=8时，该值默认为CUP核数，当>8时，该值默认为5*CPU/8+3；G1情况下，JVM堆最小空闲比率40，最大空闲比率70。新生代和老年代的相关参数有较大变化，另外可以看到Region设置1MB，另外可以看到Eden、Survivor以及Old区都有表示大小是几个regions。


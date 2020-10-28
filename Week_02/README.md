学习笔记



GC日志解读与分析
======

**串行GC，通过`-XX:+UseSerialGC`参数开启**，通过`-XX:+PrintGCDetails -XX:+PrintGCDateStamps`参数打印带有时间戳的GC日志，通过`-Xloggc:gc.demo.log`参数指定GC日志输出到某个文件内，这里是gc.demo.log文件。

```java
2020-10-26T08:01:54.096+0800: 0.427: [GC (Allocation Failure) 2020-10-26T08:01:54.096+0800: 0.427: [DefNew: 139776K->17472K(157248K), 0.0395084 secs] 139776K->57031K(506816K), 0.0396923 secs] [Times: user=0.01 sys=0.01, real=0.04 secs] 
```

> 上述是使用串行垃圾收集器的一次Minor GC，GC发生的时间`2020-10-26T08:01:54.096`，`+0800`为东八时区，`0.427`为GC事件相对于JVM启动的时间间隔，发生的原因是分配内存失败`Allocation Failure`，年轻代`DefNew`（Default New Generation）使用串行收集器回收空间，使用空间从139776KB减少到17472KB，总的年轻代可用内存为157248KB，GC耗时约39.7ms；堆（heap）的内存使用从139776KB减少到57031KB，总的堆可用内存为506816KB，总GC耗时约40ms；可以根据上面的堆和新生代的信息，可以得出此处GC，有57031-17472=39559KB的对象晋升老年代，且老年代总的可用空间为506816KB-157248KB=349568KB。

-  user —— 进程执行用户态代码所使用的时间。这是执行此进程所使用的实际 CPU 时间，其他进程和此进程阻塞的时间并不包括在内。在垃圾收集的情况下，表示 GC 线程执行所使用的 CPU 总时间。 
-  sys —— 进程在内核态消耗的 CPU 时间，即在内核执行系统调用或等待系统事件所使用的 CPU 时间。
-  real —— 程序从开始到结束所用的时钟时间。这个时间包括其他进程使用的时间片和进程阻塞的时间（比如等待 I/O 完成）。 

> [Times: user=0.01 sys=0.01, real=0.04 secs]，`user`部分表示所有 GC线程消耗的CPU时间，此处为10ms，`sys`部分表示系统调用和系统等待事件消耗的时间，此处为10ms，`real`表示应用程序GC暂停的时间，此处为40ms。

```java
2020-10-26T08:07:43.116+0800: 0.767: [Full GC (Allocation Failure) 2020-10-28T08:07:43.116+0800: 0.767: [Tenured: 174685K->174708K(174784K), 0.0344185 secs] 253204K->192073K(253440K), [Metaspace: 3291K->3291K(1056768K)], 0.0345011 secs] [Times: user=0.05 sys=0.00, real=0.05 secs] 
```

> 上述是使用串行垃圾收集器的一次Full GC，此次Full GC发生的原因是分配内存失败`Allocation Failure`，仅回收了老年代`Tenured`空间，使用空间从174685KB增加到174708KB，总的老年代可用内存为174784KB，GC耗时约34.4ms；堆（heap）的内存使用从253204KB减少到192073KB，总的堆可用内存为253440KB，总的GC时间34.5ms。这次GC仅记录老年代回收情况，老年代使用空间增加，堆使用空间反而减少，是记录存在问题，漏了年轻代？



**并行GC，通过`-XX:+UseParallelGC`参数开启**

```
2020-10-26T08:29:49.726+0800: 0.449: [GC (Allocation Failure) [PSYoungGen: 65467K->10740K(76288K)] 65467K->20801K(251392K), 0.0046561 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
```

> 上述是使用并行垃圾收集器的一次Minor GC，年轻代使用PS（Parallel Scavenge）收集器，会触发STW，使用空间从65467KB减少到10740KB，总的年轻代可用内存为76288KB，GC耗时约4.65ms

```
2020-10-26T21:45:53.744+0800: 1.275: [Full GC (Ergonomics) [PSYoungGen: 28811K->0K(116736K)] [ParOldGen: 315346K->272215K(349696K)] 344158K->272215K(466432K), [Metaspace: 4042K->4042K(1056768K)], 0.0664040 secs] [Times: user=0.34 sys=0.00, real=0.07 secs] 
```
> 上述是并行垃圾收集器的一次Full GC，同时包括年轻代和老年代，会触发STW，此次Full GC发生的原因是Ergonomics，在JVM的垃圾收集器中，Ergonomics是负责自动调解GC暂停时间和吞吐量之间平衡的一种机制，目的让虚拟机性能更好的一种做法。对于注重吞吐量的收集器来说，在某个generation被过度使用之前，GC Ergonomics就会启动一次GC。
>
> 本次Full GC正是在使用Parallel Scavenge收集器的情况下发生的，而Parallel Scavenge是一款注重吞吐量的收集器：
>
> Parallel Scavenge的目标是达到一个可控的吞吐量，吞吐量=程序运行时间/（程序运行时间+GC时间），如程序运行了99s，GC耗时1s，吞吐量=99/（99+1）=99%。Parallel Scavenge提供了两个参数用以精确控制吞吐量，分别是用以控制最大GC停顿时间的-XX:MaxGCPauseMillis及直接控制吞吐量的参数-XX:GCTimeRatio，调小最大GC停顿时间参数，并不意味一定GC速度变快。
>
> 老年代使用ParOld（Parallel Old）收集器，使用空间从315346KB减少到272215KB，总的老年代可用内存为349696KB，总的堆已使用内存从344158KB减少到272215KB，总的堆内存为466432KB，总的GC耗时约66.4ms


**CMS GC，通过`-XX:+UseConcMarkSweepGC`参数开启**

```java
2020-10-26T21:57:42.556+0800: 0.451: [GC (Allocation Failure) 2020-10-26T21:57:42.557+0800: 0.451: [ParNew: 139683K->17471K(157248K), 0.0391235 secs] 139683K->61298K(506816K), 0.0396704 secs] [Times: user=0.03 sys=0.14, real=0.04 secs]
```

> 上述是使用CMS（Concurrent Mark and Sweep）垃圾收集器的一次Young区 GC，GC发生的原因是分配内存失败，年轻代使用ParNew GC，伴随STW，使用空间从139683KB减少到17471KB，总的年轻代可用内存为157248KB，堆（heap）的内存使用从139683KB减少到61298KB，总的可用内存为506816KB，GC耗时约39ms。

```
2020-10-26T21:57:42.843+0800: 0.738: [GC (CMS Initial Mark) [1 CMS-initial-mark: 217709K(349568K)] 235419K(506816K), 0.0004813 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-10-26T21:57:42.844+0800: 0.738: [CMS-concurrent-mark-start]
2020-10-26T21:57:42.848+0800: 0.742: [CMS-concurrent-mark: 0.004/0.004 secs] [Times: user=0.05 sys=0.00, real=0.00 secs] 
2020-10-26T21:57:42.848+0800: 0.743: [CMS-concurrent-preclean-start]
2020-10-26T21:57:42.849+0800: 0.744: [CMS-concurrent-preclean: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-10-26T21:57:42.849+0800: 0.744: [CMS-concurrent-abortable-preclean-start]
2020-10-26T21:57:42.875+0800: 0.770: [GC (Allocation Failure) 2020-10-26T21:57:42.876+0800: 0.770: [ParNew: 157247K->17471K(157248K), 0.0508548 secs] 374957K->294447K(506816K), 0.0510489 secs] [Times: user=0.16 sys=0.01, real=0.05 secs] 
2020-10-26T21:57:42.964+0800: 0.858: [GC (Allocation Failure) 2020-10-26T21:57:42.964+0800: 0.858: [ParNew: 157247K->17470K(157248K), 0.0470990 secs] 434223K->350090K(506816K), 0.0472934 secs] [Times: user=0.22 sys=0.03, real=0.05 secs] 
2020-10-26T21:57:43.011+0800: 0.906: [CMS-concurrent-abortable-preclean: 0.007/0.162 secs] [Times: user=0.44 sys=0.06, real=0.16 secs] 
2020-10-26T21:57:43.011+0800: 0.906: [GC (CMS Final Remark) [YG occupancy: 20626 K (157248 K)]2020-10-26T21:57:43.011+0800: 0.906: [Rescan (parallel) , 0.0014089 secs]2020-10-26T21:57:43.013+0800: 0.908: [weak refs processing, 0.0000484 secs]2020-10-26T21:57:43.013+0800: 0.908: [class unloading, 0.0006517 secs]2020-10-26T21:57:43.014+0800: 0.908: [scrub symbol table, 0.0010814 secs]2020-10-26T21:57:43.015+0800: 0.910: [scrub string table, 0.0003597 secs][1 CMS-remark: 332619K(349568K)] 353245K(506816K), 0.0038495 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-10-26T21:57:43.015+0800: 0.910: [CMS-concurrent-sweep-start]
2020-10-26T21:57:43.017+0800: 0.912: [CMS-concurrent-sweep: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-10-26T21:57:43.017+0800: 0.912: [CMS-concurrent-reset-start]
2020-10-26T21:57:43.018+0800: 0.913: [CMS-concurrent-reset: 0.001/0.001 secs] [Times: user=0.06 sys=0.00, real=0.00 secs] 
```

> 上述是使用CMS（Concurrent Mark and Sweep）垃圾收集器的一次CMS GC发生，各个阶段情况：
>
> 阶段1，初始标记`CMS Initial Mark`，伴随着短暂STW，标记所有根对象，包括根对象直接引用的对象和年轻代中存活对象所引用（老年代）的对象；上述老年代已使用217709KB，老年代总的空间为349568KB，可以估算CMS启动阈值为0.63，堆已使用 235419KB，总的可用空间为506816KB，该阶段耗时0.4813ms，非常短。
>
> 阶段2，并发标记`CMS-concurrent-mark`，与业务线程并发执行，从根对象往下标记存活对象，由于标记过程，业务线程也在运行，存在一些已遍历对象的引用关系发生变化，JVM会通过Card的方式将该区域标记为Dirty Card，持续时间为4ms
>
> 阶段3，并发预清理`CMS-concurrent-preclean`，与业务线程并发执行，把上阶段Dirty Card的对象以及可达的对象重新遍历标记，完成后清楚Dirty Card标记，耗时2ms。在这之后`CMS-concurrent-abortable-preclean`，是为最终标记前做的准备工作，这个阶段会反复做相同事情直到满足条件发生abort，条件可能是多种，满足其中一种就会触发abort，耗时了7ms
>
> 阶段4，最终标记`CMS Final Remar`，通过STW完成标记老年代所有存活对象标记，毕竟并行标记的速度不能跟上对象变化的速度，并行标记只是减负；年轻代已使用20626K B，总的可用为157248 KB，通常CMS 会尝试在年轻代尽可能空的情况下执行Final Remark 阶段，以免连续触发多次STW 事件。`Rescan (parallel)`是扫描对象的用时总计，重新从根对象扫描，总计耗时1.4ms；`weak refs processing`是对弱引用的处理，耗时0.048ms；`class unloading`是无用类的卸载，耗时0.65ms；`scrub symbol table`和`scrub string table`分别表示清理包含类级元数据和内部化字符串的符号和字符串表的耗时，为1.08ms和0.6ms；`CMS-remark`则表示整个最终标记后，老年代使用了332619KB，总的老年代可用为349568KB，堆总的已使用353245KB，总的可用506816KB，总的耗时约为3.8ms
>
> 阶段5，并发清除`CMS-concurrent-sweep`，与业务线程并发执行，将前面标记阶段中未被标记的对象空间回收，耗时2ms
>
> 阶段6，并发重置`CMS-concurrent-reset`，与业务线程并发执行，重新设置CMS算法内部的数据结构，准备下一个CMS生命周期的使用，耗时1ms

这里可以发现。CMS为了避免老年代收集时出现长时间暂停，主要通过1）不对老年代进行整理，2）中间使用并发标记清除，让GC线程与应用线程一起运行。默认情况下，CMS使用的并发线程数为CPU核心数的1/4。

CMS收集器在老年代内存使用到一定程度时就触发垃圾回收，这是因为CMS收集器的一个缺陷导致的这种设定，也就是无法处理“浮动垃圾”，“浮动垃圾”就是指标记清除阶段用户线程运行产生的垃圾，而这部分对象由于没有做标记处理所以在本次CMS收集中是无法处理的。如果CMS是在老年代空间快用完时才启动垃圾回收，那很可能会导致在并发阶段由于用户线程持续产生垃圾而导致老年代内存不够用而导致“Concurrent Mode Failure”失败，那这时候虚拟机就会启用后备预案，临时启用Serial Old收集器来重新进行老年代的垃圾收集，Serial Old是基于“标记-整理”算法的单线程收集器，这样停顿时间就会很长。



**G1 GC，通过`-XX:+UseG1GC`参数开启**

```
2020-10-26T22:02:12.820+0800: 0.668: [GC pause (G1 Evacuation Pause) (young) (initial-mark) 370M->330M(512M), 0.0132258 secs]
2020-10-26T22:02:12.834+0800: 0.681: [GC concurrent-root-region-scan-start]
2020-10-26T22:02:12.834+0800: 0.681: [GC concurrent-root-region-scan-end, 0.0000583 secs]
2020-10-26T22:02:12.834+0800: 0.681: [GC concurrent-mark-start]
2020-10-26T22:02:12.837+0800: 0.684: [GC concurrent-mark-end, 0.0032429 secs]
2020-10-26T22:02:12.837+0800: 0.685: [GC remark, 0.0108115 secs]
2020-10-26T22:02:12.848+0800: 0.696: [GC cleanup 346M->333M(512M), 0.0017238 secs]
2020-10-26T22:02:12.850+0800: 0.697: [GC concurrent-cleanup-start]
2020-10-26T22:02:12.850+0800: 0.698: [GC concurrent-cleanup-end, 0.0000355 secs]
2020-10-26T22:02:12.861+0800: 0.709: [GC pause (G1 Evacuation Pause) (young) 403M->350M(512M), 0.0122748 secs]
2020-10-26T22:02:12.875+0800: 0.723: [GC pause (G1 Evacuation Pause) (mixed) 362M->313M(512M), 0.0071354 secs]
```

> 上述是使用G1（Garbage-First）垃圾收集器的一次G1 GC发生，各个阶段情况：
>
> 阶段1，初始标记`initial-mark`，为了充分利用STW（YoungGC都会STW），这个阶段作为年轻代垃圾收集过程的一部分进行标记，这次空间从370MB减少到330MB，总的堆可用空间为512MB，耗时约为13.2ms
>
> 阶段2，Root区扫描`concurrent-root-region-scan`，与业务线程并发执行，根分区扫描主要扫描新的survivor区，找到分区内对象指向当前分区的引用，并做记录，此过程耗时0.058ms
>
> 阶段3，并发标记`concurrent-mark`，与业务线程并发执行，使用的线程默认值是Parallel Thread个数的25%，此过程耗时3.24ms
>
> 阶段4，重新标记`remark`，并发标记过程会STW，标记所有存活对象，这和CMS最终标记类似，由于并发标记的对象引用，可能被业务线程改变，用STW最终确认引用关系，耗时10.8ms
>
> 阶段5，清理`cleanup`，也会STW，清除一个存活对象都没有的分区，并为下个并发标记阶段准备，这次空间从346MB减少到333MB，总的堆可用空间为512MB，耗时约为1.7ms
>
> 阶段6，并发清除`concurrent-cleanup`，与业务线程并发执行，用于收尾阶段5的工作，耗时0.035ms

G1 GC设计的目标是，将STW暂停时间和分布，变为可预期和可配置；首先堆不再分年轻代和老年代，而是划分为多个可存放对象的region，每个region可能定义为Eden区，也可能被定义为Survivor区，所有的Eden区+Survivor区合起来称为年轻代；G1每次回收时，只处理部分内存块，称为Collection Set，每次GC STW都会收集所有年轻代和部分老年代内存情况；在并发阶段增加了估算每个region存活对象的总数，垃圾最多的region优先收集。

G1是为了大堆设计，至少需要6GB；默认的并行GC线程数ParallelGCThread，在8核及以下的机器是CPU数量，超过8核则是CPU数量的5/8，并发标记线程数ConcGCThreads是ParallelGCThread的1/4。

JVM 线程堆栈数据分析  
===

java线程模型可以分为三个层面，java层Thread对象，JVM层的Java Thread，OS Thread，Stack，TLAB等，OS层的线程和线程生命周期。一个完整的线程流程，启动->执行->结束，涉及到各层情况如下所示：

Thread#start()（java层） -> JavaThread（JVM层）-> 系统线程（OS层）-> OSThread（JVM层）-> Stack（JVM层）-> TLAB（JVM层）-> 启动（JVM层）-> Thread#run()（java层） ->  终止（JVM层） ->  Terminate（OS层）

JVM 内部线程主要分为以下几种：

* VM 线程：单例的VMThread 对象，负责执行VM 操作；
* 定 时任务线程：单例的WatcherThread 对象， 模拟在VM 中执行定时操作的计时器中断；
*  GC 线程：垃圾收集器中，用于支持并行和并发垃圾回收的线程；
*  编译器线程： 将字节码编译为本地机器代码，主要代码优化；
*  信号分发线程：等待进程指示的信号，并将其分配给Java级别的信号处理方法。

安全点，处于安全点时，除了VM线程能活动，其余线程全部静止

1. 线程处于安全点状态：线程暂停执行，这个时候线程栈不再发生改变；
2. JVM 的安全点状态：所有线程都处于安全点状态。

常用工具：
| 类型     | 常用工具 |
| ------------ | ------ |
| JDK 工具 | jstack 工具, jcmd 工具, jconsole,jvisualvm, Java Mission Control |
| Shell 命令或者系统控制台 | Linux 的kill -3,Windows 的Ctrl + Break |
| JMX 技术 | ThreadMxBean |


内存分析与相关工具
====

**java对象空间=对象头+对象体**，有时还有外部对齐（alignment）。

**对象头**包含：

标记字（mark word），存储对象自身的运行时数据，如GC分代年龄，线程持有锁等，长度在32位JVM上是4字节，在64位JVM上是8字节（未开启指针压缩）

Class指针（kclass），存储对象指向它类元数据的指针，JVM通过这个指针确定对象是哪个类的实例，长度在32位JVM上是4字节，在64位JVM上是8字节（未开启指针压缩）

数组长度，int值，占4字节，仅数组独有

**对象体**包含：

实例数据，对象内定义的字段，不同类型字段长度不一样

内部填充（padding），由于虚拟机内存管理体系要求 Java 对象内存起始地址必须为 8 的整数倍，换句话说，Java 对象大小必须为 8 的整数倍，当对象头+实例数据大小不为 8 的整数倍时，将会使用Padding机制进行填充。

**对象体里排列优先顺序**：parent整体，填充对齐，8|4|2|1字段，填充对齐，原生类型，填充对齐

实例数据可能是基本类型，也可能是对象，具体占用大小如下所示

| Primitive Type     | Memory Required(bytes)  |
| ------------ | ------ |
|   boolean |1 |
| byte |1 |
| short |2 |
| char |2 |
| int |4 |
| float |4 |
| long |8 |
| double |8 |
| Reference |4 |

通常在32位JVM，以及内存小于-Xmx32G 的64位JVM 上(默认开启指针压缩)，这会减少对象头的大小。这里可以知道包装类比基本类型要多消耗内存很多，比如Integer=对象头（8+8）+对象体（4+4填充）=16，基本类型占4。

**多维数组**，比如在二维数组int\[dim1]\[dim2]中。每一个嵌套的数组int[dim2]都是一个单独的Object，会额外占用16个字节空间，维度越高，开销越明显，int\[128]\[2]实例占用3600字节，而int[256]实例仅占用1040字节，由此在创建多维数组时，前面的维数代表着要创建的对象数，应该尽量减少它的值，放大后面值。

**String**，String对象随着内部字符数组的增加而变长，String类对象有24字节额外开销（8+8+4+4）



**内存相关的异常**

- OutOfMemoryError：java heap space

   产生原因，1）真实内存不够，2）超出预期的访问量/数据量，3）内存泄漏

   解决办法，增加heap大小

- OutOfMemoryError：PermGen space/OutOfMemoryError：Metaspace

   产生原因，PermGen space主要原因是加载到内存中的class数量太多或体积太大，超出PermGen大小

   解决办法，增加PermGen/Metaspace

- OutOfMemoryError：Unable to create new native thread

   产生原因，程序创建的线程数量已达到上限值

   解决办法，1）调整系统参数ulimit -a，echo 120000 > /proc/sys/kernel/threads-max，2）降低xss等参数，3）调整代码，改变线程创建和使用方式

内存泄漏：指存在无法回收的对象

内存溢出：指超出堆空间

**常用工具**

- [Eclipse MAT](https://www.eclipse.org/mat/downloads.php)

- jhat

JVM问题分析调优经验
====

JVM调优的方面或注意的指标：

1. **高分配速率(High Allocation Rate)**：分配速率(Allocation rate)表示单位时间内分配的内存量。通常使用MB/sec 作为单位。上一次垃圾收集之后，与下一次GC 开始之前的年轻代使用量，两者的差值除以时间,就是分配速率。分配率过高，会导致巨大的GC开销，严重影响程序性能。

   正常系统：分配速率较低于回收速率，系统健康

   内存泄漏：分配速率持续大于回收速率，OOM

   性能劣化：分配速率较高于回收速率，系统亚健康

   总的来说，分配速率直接影响Eden区（new对象），影响Minor GC次数和时间，进而影响吞吐量，有些情况可以考虑增加年轻代大小，降低这部分的影响
   
   举例说明分配率计算

```java
2020-10-28T10:25:37.878+0800: 0.475: [GC (Allocation Failure) [PSYoungGen: 65504K->10748K(76288K)] 65504K->30648K(251392K), 0.0402412 secs] [Times: user=0.01 sys=0.09, real=0.04 secs] 
2020-10-28T10:25:37.936+0800: 0.533: [GC (Allocation Failure) [PSYoungGen: 76222K->10751K(76288K)] 96122K->60528K(251392K), 0.0306033 secs] [Times: user=0.03 sys=0.16, real=0.03 secs] 
```

|Event |time |Young before |Young after |Allocated during |Allocation rate |
| --------- | -------- |-------- |-------- |-------- |-------- |
| 1st GC        | 0.475s |65504KB|10748KB|65504KB|138MB/s|
| 2st GC        | 0.533s |76222KB|10751KB|65474KB|1128MB/s|

2. **过早提升(Premature Promotion)**，提升速率（promotion rate）用于衡量单位时间内从年轻代提升到老年代的数据量。一般使用MB/sec 作为单位, 和分配速率类似。可能存在一种情况，老年代中不仅有存活时间长的对象，也可能有存活时间短的对象，这就是过早提升。这就造成老年代的major GC不仅要回收原本存活时间长的对象，还得回收这些存活时间短的对象，这就导致GC暂停时间过长，影响吞吐量。

   过早提升的表现为：

   * 短时间内频繁Full GC
   * 每次Full GC后老年代的使用率都很低，在10%-20%或以下
   * 提升速率快接近分配速率

   解决办法，让临时数据能在年轻代存放得下：

   * 增加年轻代的大小
   * 减少每次批处理的数量

| Event  | time   | Young decreased | Total decreased | Promotion rate |
| ------ | ------ | --------------- | --------------- | -------------- |
| 1st GC | 0.475s | 54756KB         | 34856KB         | 41.9MB/s       |
| 2st GC | 0.533s | 65471KB         | 35594KB         | 515MB/s        |

GC疑难情况问题分析
====

**GC疑难问题常用分析角度**：

1、查询业务日志，可以发现这类问题：请求压力大，波峰，遭遇降级，熔断等等， 基础服务、外部API依赖。

2、查看系统资源和监控信息：
硬件信息、操作系统平台、系统架构；
排查CPU 负载、内存不足，磁盘使用量、硬件故障、磁盘分区用满、IO 等待、IO 密集、丢数据、并发竞争等情况；
排查网络：流量打满，响应超时，无响应，DNS 问题，网络抖动，防火墙问题，物理故障，网络参数调整、超时、连接数。

3、查看性能指标，包括实时监控、历史数据。可以发现假死，卡顿、响应变慢等现象；
排查数据库， 并发连接数、慢查询、索引、磁盘空间使用量、内存使用量、网络带宽、死锁、TPS、查询数据量、redo日志、undo、binlog 日志、代理、工具BUG。可以考虑的优化包括： 集群、主备、只读实例、分片、分区；
大数据，中间件，JVM 参数。

4、排查系统日志， 比如重启、崩溃、Kill 。

5、APM，比如发现有些链路请求变慢等等。

6、排查应用系统
排查配置文件: 启动参数配置、Spring 配置、JVM 监控参数、数据库参数、Log 参数、APM 配置、内存问题，比如是否存在内存泄漏，内存溢出、批处理导致的内存放大、GC 问题等等；
GC 问题， 确定GC 算法、确定GC 的KPI，GC 总耗时、GC 最大暂停时间、分析GC 日志和监控指标： 内存分配速度，分代提升速度，内存使用率等数据。适当时修改内存配置；
排查线程, 理解线程状态、并发线程数，线程Dump，锁资源、锁等待，死锁；
排查代码， 比如安全漏洞、低效代码、算法优化、存储优化、架构调整、重构、解决业务代码BUG、第三方库、XSS、CORS、正则；
单元测试： 覆盖率、边界值、Mock 测试、集成测试。

7、排除资源竞争、坏邻居效应

8、疑难问题排查分析手段

DUMP 线程\内存；

抽样分析\调整代码、异步化、削峰填谷。

Java Socket编程
====

首先Socket通信模型，包括Server端，Client端

![](D:\java00\week02\socket.PNG)

简单实现Socket编程

```java
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        while (true) {
            try {
                //创建一个ServerSocket，绑定8801端口
                final ServerSocket serverSocket = new ServerSocket(8801);
                //当有客户端请求时通过accept方法拿到Socket，进而可以进行处理
                final Socket socket = serverSocket.accept();
                executorService.execute(() -> service(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void service(Socket socket) {
        try {
            //开始通信
            Thread.sleep(20);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            printWriter.println();
            printWriter.write("hello nio!");
            printWriter.close();
            //结束通信
            socket.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
```

线程池的大小，根据用于CPU计算型还是I/O型，CPU计算型一般可设置为CPU核心数+1，I/O型可设置为CPU核数\*CPU利用率\*(1 + 等待时间/计算时间)

可通过SuperBenchmarker压测工具对实现的通信进行吞吐量性能的测试。

压测工具[**SuperBenchmarker**](https://github.com/aliostad/SuperBenchmarker)，相关参数解释


- -c --concurrency (Default: 1) 并发请求数
- -n, --numberOfRequests (Default: 100) 请求数量
- -m, --method (Default: GET) HTTP Method
- -u, --url 请求地址，也就是需要压测的地址
- -v,--verbose：输出详细
- -h, --headers：输出HTTP Header
- -k, --cookies：输出cookie
- -q, --onlyRequest：仅输出请求信息

深入讨论IO
====

上面的通信模型过程中，主要存在两种类型操作，1）CPU计算/业务处理；2）IO操作与等待/网络、磁盘、数据库。对于通过网络访问的IO应用来说，存在大部分CPU等待资源情况，属于浪费；此外还面临线程/CPU问题，数据来回复制问题，都是资源浪费，所有对于每个业务处理过程，使用一个线程一干到底，性能不是最优。

IO模型与相关概念
====

基于上面的IO问题，提出了IO模型解决。

这里先介绍一下同步，异步，阻塞，非阻塞的概念

- 同步异步是通信模式。
- 阻塞、非阻塞是线程处理模式。

不同IO模型与同步，异步，阻塞，非阻塞的关系

![](D:\java00\week02\IO_model.PNG)

> **IO模型1**

阻塞I/O，一般通过在while(true) 循环中服务端会调用accept() 方法等待接收客户端的连接的方式监听请求，请求一旦接收到一个连接请求，就可以建立通信套接字在这个通信套接字上进行读写操作，此时不能再接收其他客户端连接请求，只能等待同当前连接的客户端的操作执行完成。

> **IO模型2**

非阻塞I/O，和阻塞IO 类比，内核会立即返回，返回后获得足够的CPU 时间继续做其它的事情。

> **IO模型3**

I/O多路复用，也称为事件驱动I/O，单个线程里同时监控多个套接字，通过select 或poll 轮询所负责的所有socket，当某个socket 有数据到达了，就通知用户进程。IO 复用同非阻塞IO 本质一样，不过利用了新的select 系统调用，由内核来负责本来是请求进程该做的轮询操作。看似比非阻塞IO 还多了一个系统调用开销，不过因为可以支持多路IO，才算提高了效率。

select/poll 的几大缺点：

* 每次调用select，都需要把fd 集合从用户态拷贝到内核态，这个开销在fd 很多时会很大
* 同时每次调用select 都需要在内核遍历传递进来的所有fd，这个开销在fd 很多时也很大
* select 支持的文件描述符数量太小了，默认是1024

epoll（Linux 2.5.44内核中引入,2.6内核正式引入,可被用于代替POSIX select 和poll 系统调用）：

* 内核与用户空间共享一块内存
* 通过回调解决遍历问题
* fd 没有限制，可以支撑10万连接

> **IO模型4**

信号驱动I/O，信号驱动IO 与BIO 和NIO 最大的区别就在于，在IO 执行的数据准备阶段，不会阻塞用户进程。也就是用户进程告诉内核需要的数据，然后去做别的事情，内核准备好数据后，就通知用户进程来取。

> **IO模型5**

异步I/O，异步IO 真正实现了IO 全流程的非阻塞。用户进程发出系统调用后立即返回，内核等待数据准备完成，然后将数据拷贝到用户进程缓冲区，然后发送信号告诉用户进程IO 操作执行完毕。相比于模型4，模型5是把数据送到了用户进程面前。



Netty框架简介
====

Netty网络应用开发框架

![](D:\java00\week02\netty.PNG)

设计原理：

1. 异步
2. 事件驱动
3. 基于NIO

适用于：

- 服务端
- 客户端
- TCP/UDP

优势：

- 高吞吐
- 低延迟
- 低开销
- 零拷贝
- 可扩容
- 松耦合
- 维护性好

Netty中核心类及概念

| 类      | 简介 |
| ------- | -------- |
| Channel | 通道，Java NIO 中的基础概念,代表一个打开的连接,可执行读取/写入IO 操作。Netty 对Channel 的所有IO 操作都是非阻塞的。        |
| ChannelFuture | Java 的Future 接口，只能查询操作的完成情况, 或者阻塞当前线程等待操作完成。Netty 封装一个ChannelFuture 接口。我们可以将回调方法传给ChannelFuture，在操作完成时自动执行。        |
| Event & Handler | Netty 基于事件驱动，事件和处理器可以关联到入站和出站数据流。        |
| Encoder & Decoder| 处理网络IO 时，需要进行序列化和反序列化, 转换Java 对象与字节流。对入站数据进行解码, 基类是ByteToMessageDecoder。对出站数据进行编码, 基类是MessageToByteEncoder。        |
| ChannelPipeline | 数据处理管道就是事件处理器链。有顺序、同一Channel 的出站处理器和入站处理器在同一个列表中。        |



**参考**

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)

[SuperBenchmarker](https://github.com/aliostad/SuperBenchmarker)

[如何选择合适的 GC 时间 —— USER, SYS, REAL？](https://cloud.tencent.com/developer/article/1491229)

[读懂一行Full GC日志](https://cloud.tencent.com/developer/article/1082687)

[理解CMS GC日志](https://www.jianshu.com/p/ba768d8e9fec)

[深入理解G1的GC日志](https://juejin.im/post/6844903893906751501#heading-3)

[一个Java对象到底占用多大内存？](https://juejin.im/post/6844904022101475342)

[netty](https://netty.io/wiki/user-guide-for-4.x.html)

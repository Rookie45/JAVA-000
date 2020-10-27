**week2-01-题目1：**

使用 GCLogAnalysis.java 自己演练一遍串行/并行/CMS/G1的案例。   

**答题如下**：

> **串行GC，堆大小512M**

```java
Java HotSpot(TM) 64-Bit Server VM (25.112-b15) for windows-amd64 JRE (1.8.0_112-b15), built on Sep 22 2016 21:31:56 by "java_re" with MS VC++ 10.0 (VS2010)
Memory: 4k page, physical 8264920k(3129848k free), swap 16527940k(7278328k free)
CommandLine flags: -XX:InitialHeapSize=536870912 -XX:MaxHeapSize=536870912 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseSerialGC 
2020-10-26T22:13:54.096+0800: 0.427: [GC (Allocation Failure) 2020-10-26T22:13:54.096+0800: 0.427: [DefNew: 139776K->17472K(157248K), 0.0395084 secs] 139776K->57031K(506816K), 0.0396923 secs] [Times: user=0.01 sys=0.01, real=0.04 secs] 
...
Heap
 def new generation   total 157248K, used 5955K [0x00000000e0000000, 0x00000000eaaa0000, 0x00000000eaaa0000)
  eden space 139776K,   4% used [0x00000000e0000000, 0x00000000e05d0fa8, 0x00000000e8880000)
  from space 17472K,   0% used [0x00000000e9990000, 0x00000000e9990000, 0x00000000eaaa0000)
  to   space 17472K,   0% used [0x00000000e8880000, 0x00000000e8880000, 0x00000000e9990000)
 tenured generation   total 349568K, used 289237K [0x00000000eaaa0000, 0x0000000100000000, 0x0000000100000000)
   the space 349568K,  82% used [0x00000000eaaa0000, 0x00000000fc515708, 0x00000000fc515800, 0x0000000100000000)
 Metaspace       used 4053K, capacity 4572K, committed 4864K, reserved 1056768K
  class space    used 454K, capacity 460K, committed 512K, reserved 1048576K
```

共发生15次GC，截取串行垃圾收集器的第一次GC，简单分析：

GC发生的时间`2020-10-26T22:13:54.096`，`+0800`为东八时区，`0.427`为GC事件相对于JVM启动的时间间隔，发生的原因是分配内存失败`Allocation Failure`，年轻代`DefNew`（Default New Generation）使用串行收集器回收空间，使用空间从139776KB减少到17472KB，总的年轻代可用内存为157248KB，GC耗时约39ms；堆（heap）的内存使用从139776KB减少到57031KB，总的可用内存为506816KB，GC耗时约40ms；可以根据上面的堆和新生代的信息，可以得出此处GC，有57031-17472=39559KB的对象晋升老年代，且老年代总的可用空间为506816KB-157248KB=349568KB。后面的时间统计，`user`部分表示所有 GC线程消耗的CPU时间，此处为10ms，`sys`部分表示系统调用和系统等待事件消耗的时间，此处为10ms，`real`表示应用程序GC暂停的时间，此处为40ms。

`def new generation`为年轻代空间使用情况，总的可用空间为157248KB（eden space + from space），已使用72766K，使用率为4%，后面[0x00000000e0000000, 0x00000000eaaa0000, 0x00000000eaaa0000)表示的内存地址。`tenured generation`为老年代空间使用情况，使用率为82%，剩下的是Metaspace和类定义的使用空间。

> **并行GC，堆大小512M**

```java
...
CommandLine flags: -XX:InitialHeapSize=536870912 -XX:MaxHeapSize=536870912 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC 
2020-10-26T21:45:52.889+0800: 0.428: [GC (Allocation Failure) [PSYoungGen: 131584K->21488K(153088K)] 131584K->59137K(502784K), 0.0998304 secs] [Times: user=0.06 sys=0.16, real=0.10 secs] 
...
2020-10-26T21:45:53.744+0800: 1.275: [Full GC (Ergonomics) [PSYoungGen: 28811K->0K(116736K)] [ParOldGen: 315346K->272215K(349696K)] 344158K->272215K(466432K), [Metaspace: 4042K->4042K(1056768K)], 0.0664040 secs] [Times: user=0.34 sys=0.00, real=0.07 secs] 
Heap
 PSYoungGen      total 116736K, used 2668K [0x00000000f5580000, 0x0000000100000000, 0x0000000100000000)
  eden space 58880K, 4% used [0x00000000f5580000,0x00000000f581b1b0,0x00000000f8f00000)
  from space 57856K, 0% used [0x00000000fc780000,0x00000000fc780000,0x0000000100000000)
  to   space 57856K, 0% used [0x00000000f8f00000,0x00000000f8f00000,0x00000000fc780000)
 ParOldGen       total 349696K, used 272215K [0x00000000e0000000, 0x00000000f5580000, 0x00000000f5580000)
  object space 349696K, 77% used [0x00000000e0000000,0x00000000f09d5e18,0x00000000f5580000)
 Metaspace       used 4048K, capacity 4572K, committed 4864K, reserved 1056768K
  class space    used 454K, capacity 460K, committed 512K, reserved 1048576K
```

共发生15次GC，3次Full GC，截取了使用并行垃圾收集器的第一次GC和一次Full GC，简单分析：

GC发生的原因是分配内存失败，年轻代`PSYoungGen`使用并行GC（Parallel Scavenge），使用空间从131584KB减少到21488KB，总的年轻代可用内存为153088KB，堆（heap）的内存使用从131584KB减少到59137KB，总的可用内存为502784KB，GC耗时约100ms；可以根据上面的堆和新生代的信息，可以得出此处GC，有59137-21488=37649KB的对象晋升老年代，且老年代总的可用空间为502784-153088=349696KB。后面的时间统计， GC线程消耗的CPU时间为60ms，线程上下文切换为160ms，应用程序暂停的时间为100ms。

Full GC发生的原因是Ergonomics，它是JVM中自动调节暂停时间和吞吐量之间的平衡，让JVM性能更好的一种策略。换句话说，如果是注重吞吐量的收集器，在某个generation被过渡使用之前，GC Ergonomics就会启动一次GC。这次Full GC，年轻代`PSYoungGen`使用空间从28811KB减少到0KB，总的年轻代可用内存为116736KB，老年代使用的并行GC（Parallel  Old），使用空间从315346KB减少到272215KB，总的老年代可用内存为349696KB，堆（heap）的内存使用从344158KB减少到272215KB，总的可用内存为466432KB，GC耗时约为66ms。Metaspace空间未发生变化。后面的时间统计， GC线程消耗的CPU时间为340ms，系统消耗为0ms，应用程序暂停的时间为70ms。

`PSYoungGen`为年轻代空间使用情况，总的可用空间为116736KB（eden space + from space），已使用2668KB，使用率为4%，后面[0x00000000f5580000, 0x0000000100000000, 0x0000000100000000)表示的内存地址。`ParOldGen`为老年代空间使用情况，使用率为77%，剩下的是Metaspace和类定义的使用空间，剩下的是Metaspace和类定义的使用空间。

> **CMS GC，堆大小512M**

```java
...
CommandLine flags: -XX:InitialHeapSize=536870912 -XX:MaxHeapSize=536870912 -XX:MaxNewSize=178958336 -XX:MaxTenuringThreshold=6 -XX:NewSize=178958336 -XX:OldPLABSize=16 -XX:OldSize=357912576 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:-UseLargePagesIndividualAllocation -XX:+UseParNewGC 
2020-10-27T14:57:42.556+0800: 0.451: [GC (Allocation Failure) 2020-10-27T14:57:42.557+0800: 0.451: [ParNew: 139683K->17471K(157248K), 0.0391235 secs] 139683K->61298K(506816K), 0.0396704 secs] [Times: user=0.03 sys=0.14, real=0.04 secs] 
...
2020-10-27T14:57:42.843+0800: 0.738: [GC (CMS Initial Mark) [1 CMS-initial-mark: 217709K(349568K)] 235419K(506816K), 0.0004813 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-10-27T14:57:42.844+0800: 0.738: [CMS-concurrent-mark-start]
2020-10-27T14:57:42.848+0800: 0.742: [CMS-concurrent-mark: 0.004/0.004 secs] [Times: user=0.05 sys=0.00, real=0.00 secs] 
2020-10-27T14:57:42.848+0800: 0.743: [CMS-concurrent-preclean-start]
2020-10-27T14:57:42.849+0800: 0.744: [CMS-concurrent-preclean: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-10-27T14:57:42.849+0800: 0.744: [CMS-concurrent-abortable-preclean-start]
2020-10-27T14:57:42.875+0800: 0.770: [GC (Allocation Failure) 2020-10-27T14:57:42.876+0800: 0.770: [ParNew: 157247K->17471K(157248K), 0.0508548 secs] 374957K->294447K(506816K), 0.0510489 secs] [Times: user=0.16 sys=0.01, real=0.05 secs] 
2020-10-27T14:57:42.964+0800: 0.858: [GC (Allocation Failure) 2020-10-27T14:57:42.964+0800: 0.858: [ParNew: 157247K->17470K(157248K), 0.0470990 secs] 434223K->350090K(506816K), 0.0472934 secs] [Times: user=0.22 sys=0.03, real=0.05 secs] 
2020-10-27T14:57:43.011+0800: 0.906: [CMS-concurrent-abortable-preclean: 0.007/0.162 secs] [Times: user=0.44 sys=0.06, real=0.16 secs] 
2020-10-27T14:57:43.011+0800: 0.906: [GC (CMS Final Remark) [YG occupancy: 20626 K (157248 K)]2020-10-27T14:57:43.011+0800: 0.906: [Rescan (parallel) , 0.0014089 secs]2020-10-27T14:57:43.013+0800: 0.908: [weak refs processing, 0.0000484 secs]2020-10-27T14:57:43.013+0800: 0.908: [class unloading, 0.0006517 secs]2020-10-27T14:57:43.014+0800: 0.908: [scrub symbol table, 0.0010814 secs]2020-10-27T14:57:43.015+0800: 0.910: [scrub string table, 0.0003597 secs][1 CMS-remark: 332619K(349568K)] 353245K(506816K), 0.0038495 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-10-27T14:57:43.015+0800: 0.910: [CMS-concurrent-sweep-start]
2020-10-27T14:57:43.017+0800: 0.912: [CMS-concurrent-sweep: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-10-27T14:57:43.017+0800: 0.912: [CMS-concurrent-reset-start]
2020-10-27T14:57:43.018+0800: 0.913: [CMS-concurrent-reset: 0.001/0.001 secs] [Times: user=0.06 sys=0.00, real=0.00 secs] 
...
Heap
 par new generation   total 157248K, used 56874K [0x00000000e0000000, 0x00000000eaaa0000, 0x00000000eaaa0000)
  eden space 139776K,  40% used [0x00000000e0000000, 0x00000000e378aa90, 0x00000000e8880000)
  from space 17472K,   0% used [0x00000000e8880000, 0x00000000e8880000, 0x00000000e9990000)
  to   space 17472K,   0% used [0x00000000e9990000, 0x00000000e9990000, 0x00000000eaaa0000)
 concurrent mark-sweep generation total 349568K, used 285403K [0x00000000eaaa0000, 0x0000000100000000, 0x0000000100000000)
 Metaspace       used 4053K, capacity 4572K, committed 4864K, reserved 1056768K
  class space    used 454K, capacity 460K, committed 512K, reserved 1048576K
```

共发生4次CMS GC，11次GC，截取了使用CMS垃圾收集器的第一次GC和一次CMS GC，简单分析：

从命令行参数可以知道，CMS默认和ParNewGC配合使用，年轻代晋升老年代的阈值为6（`-XX:MaxTenuringThreshold`）。

GC发生的原因是分配内存失败，年轻代使用并行GC（ParNew），使用空间从139683KB减少到17471KB，总的年轻代可用内存为157248KB，堆（heap）的内存使用从139683KB减少到61298KB，总的可用内存为506816KB，GC耗时约39ms；GC线程消耗的CPU时间为30ms，系统消耗为140ms，应用程序暂停的时间为40ms。

CMS GC发生时，各个阶段情况：

阶段1，初始标记`CMS Initial Mark`，伴随着短暂STW，标记所有根对象，老年代已使用217709KB，老年代总的空间为349568KB，堆已使用 235419KB，总的可用空间为506816KB， 耗时0.4813ms，非常短。

阶段2，并发标记`CMS-concurrent-mark`，与业务线程并发执行，从根对象往下标记存活对象，持续时间为4ms

阶段3，并发预清理`CMS-concurrent-preclean`，与业务线程并发执行，由于标记过程，业务线程也在运行，存在一些引用关系发生变化，JVM会通过Card的方式将该区域标记为Dirty Card，耗时2ms。在这之后`CMS-concurrent-abortable-preclean`，是为最终标记前做的准备工作，这个阶段会反复做相同事情直到满足条件发生abort，条件可能是多种，满足其中一种就会触发abort，耗时了7ms

阶段4，最终标记`CMS Final Remar`，伴随STW，标记老年代所有存活对象标记，年轻代已使用20626 K B，总的可用为157248 KB，`Rescan (parallel)`是扫描对象的用时总计，重新从根对象扫描，总计耗时1.4ms；`weak refs processing`是对弱引用的处理，耗时0.048ms；`class unloading`是无用类的卸载，耗时0.65ms；`scrub symbol table`和`scrub string table`分别表示清理包含类级元数据和内部化字符串的符号和字符串表的耗时，为1.08ms和0.6ms；`CMS-remark`则表示整个最终标记后，老年代使用了332619KB，总的老年代可用为349568KB，堆总的已使用353245KB，总的可用506816KB，总的耗时约为3.8ms

阶段5，并发清除`CMS-concurrent-sweep`，与业务线程并发执行，将前面标记阶段中未被标记的对象空间回收，耗时2ms

阶段6，并发重置`CMS-concurrent-reset`，与业务线程并发执行，重新设置CMS算法内部的数据结构，准备下一个CMS生命周期的使用，耗时1ms

> **G1 GC，堆大小512M**

```java
...
CommandLine flags: -XX:InitialHeapSize=536870912 -XX:MaxHeapSize=536870912 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC -XX:-UseLargePagesIndividualAllocation 
2020-10-27T15:02:12.463+0800: 0.310: [GC pause (G1 Evacuation Pause) (young) 25M->11M(512M), 0.0074881 secs]
...
2020-10-27T15:02:12.820+0800: 0.668: [GC pause (G1 Evacuation Pause) (young) (initial-mark) 370M->330M(512M), 0.0132258 secs]
2020-10-27T15:02:12.834+0800: 0.681: [GC concurrent-root-region-scan-start]
2020-10-27T15:02:12.834+0800: 0.681: [GC concurrent-root-region-scan-end, 0.0000583 secs]
2020-10-27T15:02:12.834+0800: 0.681: [GC concurrent-mark-start]
2020-10-27T15:02:12.837+0800: 0.684: [GC concurrent-mark-end, 0.0032429 secs]
2020-10-27T15:02:12.837+0800: 0.685: [GC remark, 0.0108115 secs]
2020-10-27T15:02:12.848+0800: 0.696: [GC cleanup 346M->333M(512M), 0.0017238 secs]
2020-10-27T15:02:12.850+0800: 0.697: [GC concurrent-cleanup-start]
2020-10-27T15:02:12.850+0800: 0.698: [GC concurrent-cleanup-end, 0.0000355 secs]
2020-10-27T15:02:12.861+0800: 0.709: [GC pause (G1 Evacuation Pause) (young) 403M->350M(512M), 0.0122748 secs]
2020-10-27T15:02:12.875+0800: 0.723: [GC pause (G1 Evacuation Pause) (mixed) 362M->313M(512M), 0.0071354 secs]
...
```

共发生7次G1 GC，25次Young GC，22次midex GC，这里GC日志参数使用的是XX:+PrintGC，所以输出并不是详情。同样截取了使用G1垃圾收集器的第一次GC和一次完整的G1 GC，简单分析：

第一次GC`(G1 Evacuation Pause) (young)`为纯年轻代模式转移暂停，从25MB减少到11MB，总的堆可用空间为512MB，耗时约为7.5ms

G1 GC发生时，各个阶段情况：

阶段1，初始标记`initial-mark`，为了充分利用STW（YoungGC都会STW），这个阶段作为新生代垃圾收集的一部分进行标记，这次空间从370MB减少到330MB，总的堆可用空间为512MB，耗时约为13.2ms

阶段2，Root区扫描`concurrent-root-region-scan`，与业务线程并发执行，根分区扫描主要扫描新的survivor区，找到分区内对象指向当前分区的引用，并做记录，此过程耗时0.058ms

阶段3，并发标记`concurrent-mark`，与业务线程并发执行，使用的线程默认值是Parallel Thread个数的25%，此过程耗时3.24ms

阶段4，重新标记`remark`，并发标记过程会STW，标记所有存活对象，这和CMS最终标记类似，由于并发标记的对象引用，可能被业务线程改变，用STW最终确认引用关系，耗时10.8ms

阶段5，清理`cleanup`，也会STW，清除一个存活对象都没有的分区，并为下个并发标记阶段准备，这次空间从346MB减少到333MB，总的堆可用空间为512MB，耗时约为1.7ms

阶段6，并发清除`concurrent-cleanup`，与业务线程并发执行，用于收尾阶段5的工作，耗时0.035ms

后面是一次纯年轻代的GC和一次混合GC（年轻代和老年代）。

> **串行 GC，堆大小1024M**

```java
...
CommandLine flags: -XX:InitialHeapSize=1073741824 -XX:MaxHeapSize=1073741824 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseSerialGC 
2020-10-27T14:34:10.959+0800: 0.627: [GC (Allocation Failure) 2020-10-27T14:34:10.959+0800: 0.627: [DefNew: 279616K->34944K(314560K), 0.1030676 secs] 279616K->107502K(1013632K), 0.1034270 secs] [Times: user=0.01 sys=0.08, real=0.10 secs] 
...
Heap
 def new generation   total 314560K, used 43468K [0x00000000c0000000, 0x00000000d5550000, 0x00000000d5550000)
  eden space 279616K,   3% used [0x00000000c0000000, 0x00000000c0853218, 0x00000000d1110000)
  from space 34944K,  99% used [0x00000000d3330000, 0x00000000d554fee8, 0x00000000d5550000)
  to   space 34944K,   0% used [0x00000000d1110000, 0x00000000d1110000, 0x00000000d3330000)
 tenured generation   total 699072K, used 464113K [0x00000000d5550000, 0x0000000100000000, 0x0000000100000000)
   the space 699072K,  66% used [0x00000000d5550000, 0x00000000f1a8c660, 0x00000000f1a8c800, 0x0000000100000000)
 Metaspace       used 4049K, capacity 4572K, committed 4864K, reserved 1056768K
  class space    used 456K, capacity 460K, committed 512K, reserved 1048576K
```

共发生6次GC，截取串行垃圾收集器的第一次GC，发生时间为JVM启动后的0.627秒，此时年轻代总的可用314560KB，总的堆可用1013632KB，耗时100ms。

> **并行GC，堆大小1024M**

```java
...
CommandLine flags: -XX:InitialHeapSize=1073741824 -XX:MaxHeapSize=1073741824 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC 
2020-10-27T14:45:03.040+0800: 0.607: [GC (Allocation Failure) [PSYoungGen: 262144K->43506K(305664K)] 262144K->98499K(1005056K), 0.0624664 secs] [Times: user=0.02 sys=0.26, real=0.06 secs] 
...
Heap
 PSYoungGen      total 232960K, used 121225K [0x00000000eab00000, 0x0000000100000000, 0x0000000100000000)
  eden space 116736K, 4% used [0x00000000eab00000,0x00000000eafe3fe0,0x00000000f1d00000)
  from space 116224K, 99% used [0x00000000f1d00000,0x00000000f8e7e668,0x00000000f8e80000)
  to   space 116224K, 0% used [0x00000000f8e80000,0x00000000f8e80000,0x0000000100000000)
 ParOldGen       total 699392K, used 552375K [0x00000000c0000000, 0x00000000eab00000, 0x00000000eab00000)
  object space 699392K, 78% used [0x00000000c0000000,0x00000000e1b6dde0,0x00000000eab00000)
 Metaspace       used 4049K, capacity 4572K, committed 4864K, reserved 1056768K
  class space    used 454K, capacity 460K, committed 512K, reserved 1048576K
```

共发生9次GC，截取并行垃圾收集器的第一次GC，发生时间为JVM启动后的0.627秒，此时年轻代总的可用305664KB，总的堆可用1005056KB，耗时60ms。

> **CMS GC，堆大小1024M**

```java
...
CommandLine flags: -XX:InitialHeapSize=1073741824 -XX:MaxHeapSize=1073741824 -XX:MaxNewSize=357916672 -XX:MaxTenuringThreshold=6 -XX:NewSize=357916672 -XX:OldPLABSize=16 -XX:OldSize=715825152 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:-UseLargePagesIndividualAllocation -XX:+UseParNewGC 
2020-10-27T14:53:47.258+0800: 0.452: [GC (Allocation Failure) 2020-10-27T14:53:47.258+0800: 0.452: [ParNew: 279545K->34943K(314560K), 0.0585589 secs] 279545K->106433K(1013632K), 0.0587993 secs] [Times: user=0.08 sys=0.19, real=0.06 secs] 
...
2020-10-27T14:53:47.695+0800: 0.890: [GC (CMS Initial Mark) [1 CMS-initial-mark: 377164K(699072K)] 418234K(1013632K), 0.0002602 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-10-27T14:53:47.695+0800: 0.890: [CMS-concurrent-mark-start]
2020-10-27T14:53:47.701+0800: 0.896: [CMS-concurrent-mark: 0.006/0.006 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
2020-10-27T14:53:47.702+0800: 0.896: [CMS-concurrent-preclean-start]
2020-10-27T14:53:47.704+0800: 0.899: [CMS-concurrent-preclean: 0.003/0.003 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-10-27T14:53:47.705+0800: 0.899: [CMS-concurrent-abortable-preclean-start]
2020-10-27T14:53:47.735+0800: 0.929: [GC (Allocation Failure) 2020-10-27T14:53:47.735+0800: 0.930: [ParNew2020-10-27T14:53:47.808+0800: 1.002: [CMS-concurrent-abortable-preclean: 0.003/0.103 secs] [Times: user=0.53 sys=0.03, real=0.10 secs] 
: 314553K->34943K(314560K), 0.0781954 secs] 691717K->505950K(1013632K), 0.0783080 secs] [Times: user=0.44 sys=0.03, real=0.08 secs] 
2020-10-27T14:53:47.813+0800: 1.008: [GC (CMS Final Remark) [YG occupancy: 35155 K (314560 K)]2020-10-27T14:53:47.813+0800: 1.008: [Rescan (parallel) , 0.0008569 secs]2020-10-27T14:53:47.814+0800: 1.009: [weak refs processing, 0.0000511 secs]2020-10-27T14:53:47.814+0800: 1.009: [class unloading, 0.0005556 secs]2020-10-27T14:53:47.815+0800: 1.010: [scrub symbol table, 0.0007677 secs]2020-10-27T14:53:47.816+0800: 1.010: [scrub string table, 0.0002109 secs][1 CMS-remark: 471007K(699072K)] 506162K(1013632K), 0.0026552 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 
2020-10-27T14:53:47.816+0800: 1.011: [CMS-concurrent-sweep-start]
2020-10-27T14:53:47.818+0800: 1.013: [CMS-concurrent-sweep: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-10-27T14:53:47.819+0800: 1.013: [CMS-concurrent-reset-start]
2020-10-27T14:53:47.822+0800: 1.016: [CMS-concurrent-reset: 0.003/0.003 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
...
Heap
 par new generation   total 314560K, used 64129K [0x00000000c0000000, 0x00000000d5550000, 0x00000000d5550000)
  eden space 279616K,  10% used [0x00000000c0000000, 0x00000000c1c80548, 0x00000000d1110000)
  from space 34944K, 100% used [0x00000000d1110000, 0x00000000d3330000, 0x00000000d3330000)
  to   space 34944K,   0% used [0x00000000d3330000, 0x00000000d3330000, 0x00000000d5550000)
 concurrent mark-sweep generation total 699072K, used 597991K [0x00000000d5550000, 0x0000000100000000, 0x0000000100000000)
 Metaspace       used 4052K, capacity 4572K, committed 4864K, reserved 1056768K
  class space    used 454K, capacity 460K, committed 512K, reserved 1048576K
```

2次CMS GC，7次GC，截取CMS垃圾收集器的第一次GC和一次完整的CMS GC，第一次GC发生时间为JVM启动后的0.452秒，此时年轻代总的可用314560KB，总的堆可用1013632KB，耗时58.8ms。

CMS GC，初始标记耗时0.26ms，并发标记耗时6ms，并发预清理耗时3+3=6ms，最终标记耗时2.65ms，并发清除耗时2ms，并发重置耗时3ms。

> **G1 GC，堆大小1024M**

```java
...
CommandLine flags: -XX:InitialHeapSize=1073741824 -XX:MaxHeapSize=1073741824 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC -XX:-UseLargePagesIndividualAllocation 
2020-10-27T15:07:54.939+0800: 0.407: [GC pause (G1 Evacuation Pause) (young) 51M->29M(1024M), 0.0138495 secs]
...
2020-10-27T15:07:55.559+0800: 1.027: [GC pause (G1 Evacuation Pause) (young) (initial-mark) 620M->570M(1024M), 0.0198185 secs]
2020-10-27T15:07:55.579+0800: 1.046: [GC concurrent-root-region-scan-start]
2020-10-27T15:07:55.579+0800: 1.047: [GC concurrent-root-region-scan-end, 0.0000664 secs]
2020-10-27T15:07:55.579+0800: 1.047: [GC concurrent-mark-start]
2020-10-27T15:07:55.585+0800: 1.052: [GC concurrent-mark-end, 0.0058810 secs]
2020-10-27T15:07:55.585+0800: 1.053: [GC remark, 0.0031161 secs]
2020-10-27T15:07:55.588+0800: 1.056: [GC cleanup 605M->530M(1024M), 0.0013166 secs]
2020-10-27T15:07:55.590+0800: 1.057: [GC concurrent-cleanup-start]
2020-10-27T15:07:55.590+0800: 1.057: [GC concurrent-cleanup-end, 0.0001154 secs]
2020-10-27T15:07:55.627+0800: 1.095: [GC pause (G1 Evacuation Pause) (young) 741M->591M(1024M), 0.0228676 secs]
2020-10-27T15:07:55.653+0800: 1.121: [GC pause (G1 Evacuation Pause) (mixed) 608M->506M(1024M), 0.0114855 secs]
...
```

2次G1 GC，4次mix GC，14次 young GC，截取G1垃圾收集器的第一次GC和一次完整的G1 GC，第一次GC发生时间为JVM启动后的0.407秒，此时年轻代51MB减少到29MB，总的堆可用1024M，耗时13.8ms。

G1 GC，初始标记耗时19.8ms，并发root扫描耗时0.067ms，并发标记耗时5.89ms，重新标记耗时3.11ms，清除耗时1.31ms，并发清除耗时0.115ms


> **串行 GC，堆大小2048M**

```java
...
CommandLine flags: -XX:InitialHeapSize=2147483648 -XX:MaxHeapSize=2147483648 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseSerialGC 
2020-10-27T14:35:13.029+0800: 0.722: [GC (Allocation Failure) 2020-10-27T14:35:13.029+0800: 0.722: [DefNew: 559232K->69888K(629120K), 0.1296727 secs] 559232K->181533K(2027264K), 0.1299272 secs] [Times: user=0.08 sys=0.06, real=0.13 secs] 
...
Heap
 def new generation   total 629120K, used 86772K [0x0000000080000000, 0x00000000aaaa0000, 0x00000000aaaa0000)
  eden space 559232K,   3% used [0x0000000080000000, 0x000000008107d538, 0x00000000a2220000)
  from space 69888K,  99% used [0x00000000a6660000, 0x00000000aaa9fd30, 0x00000000aaaa0000)
  to   space 69888K,   0% used [0x00000000a2220000, 0x00000000a2220000, 0x00000000a6660000)
 tenured generation   total 1398144K, used 382191K [0x00000000aaaa0000, 0x0000000100000000, 0x0000000100000000)
   the space 1398144K,  27% used [0x00000000aaaa0000, 0x00000000c1fdbcf0, 0x00000000c1fdbe00, 0x0000000100000000)
 Metaspace       used 4054K, capacity 4572K, committed 4864K, reserved 1056768K
  class space    used 456K, capacity 460K, committed 512K, reserved 1048576K
```

共发生3次GC，截取串行垃圾收集器的第一次GC，发生时间为JVM启动后的0.722秒，此时年轻代总的可用629120KB，总的堆可用2027264KB，耗时130ms。

> **并行GC，堆大小2048M**

```java
...
CommandLine flags: -XX:InitialHeapSize=2147483648 -XX:MaxHeapSize=2147483648 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC 
2020-10-27T14:47:10.897+0800: 0.684: [GC (Allocation Failure) [PSYoungGen: 524800K->87038K(611840K)] 524800K->169821K(2010112K), 0.0962240 secs] [Times: user=0.03 sys=0.28, real=0.10 secs] 
...
Heap
 PSYoungGen      total 611840K, used 527462K [0x00000000d5580000, 0x0000000100000000, 0x0000000100000000)
  eden space 524800K, 83% used [0x00000000d5580000,0x00000000f039d4f0,0x00000000f5600000)
  from space 87040K, 99% used [0x00000000fab00000,0x00000000ffffc440,0x0000000100000000)
  to   space 87040K, 0% used [0x00000000f5600000,0x00000000f5600000,0x00000000fab00000)
 ParOldGen       total 1398272K, used 448452K [0x0000000080000000, 0x00000000d5580000, 0x00000000d5580000)
  object space 1398272K, 32% used [0x0000000080000000,0x000000009b5f1020,0x00000000d5580000)
 Metaspace       used 4050K, capacity 4572K, committed 4864K, reserved 1056768K
  class space    used 454K, capacity 460K, committed 512K, reserved 1048576K
```

共发生4次，截取并行垃圾收集器的第一次GC，发生时间为JVM启动后的0.684秒，此时年轻代总的可用611840KB，总的堆可用2010112KB，耗时96ms。

> **CMS GC，堆大小2048M**

```java
...
CommandLine flags: -XX:InitialHeapSize=2147483648 -XX:MaxHeapSize=2147483648 -XX:MaxNewSize=697933824 -XX:MaxTenuringThreshold=6 -XX:NewSize=697933824 -XX:OldPLABSize=16 -XX:OldSize=1395867648 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:-UseLargePagesIndividualAllocation -XX:+UseParNewGC 
2020-10-27T14:52:21.551+0800: 0.620: [GC (Allocation Failure) 2020-10-27T14:52:21.551+0800: 0.621: [ParNew: 545344K->68095K(613440K), 0.0808178 secs] 545344K->170503K(2029056K), 0.0810750 secs] [Times: user=0.05 sys=0.31, real=0.08 secs] 
...
Heap
 par new generation   total 613440K, used 431189K [0x0000000080000000, 0x00000000a9990000, 0x00000000a9990000)
  eden space 545344K,  66% used [0x0000000080000000, 0x0000000096295900, 0x00000000a1490000)
  from space 68096K,  99% used [0x00000000a1490000, 0x00000000a570fc40, 0x00000000a5710000)
  to   space 68096K,   0% used [0x00000000a5710000, 0x00000000a5710000, 0x00000000a9990000)
 concurrent mark-sweep generation total 1415616K, used 511889K [0x00000000a9990000, 0x0000000100000000, 0x0000000100000000)
 Metaspace       used 4050K, capacity 4572K, committed 4864K, reserved 1056768K
  class space    used 456K, capacity 460K, committed 512K, reserved 1048576K
```

共发生4次ParNew GC，截取CMS垃圾收集器的第一次GC，第一次GC发生时间为JVM启动后的0.620秒，此时年轻代总的可用613440KB，总的堆可用2029056KB，耗时80.8ms。

> **G1 GC，堆大小2048M**

```java
...
CommandLine flags: -XX:InitialHeapSize=2147483648 -XX:MaxHeapSize=2147483648 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC -XX:-UseLargePagesIndividualAllocation 
2020-10-27T15:10:36.432+0800: 0.461: [GC pause (G1 Evacuation Pause) (young) 102M->46M(2048M), 0.0234379 secs]
...
```

13次young GC，截取G1垃圾收集器的第一次GC，第一次GC发生时间为JVM启动后的0.461秒，此时年轻代102MB减少到46MB，总的堆可用2048M，耗时23.4ms。

> **串行 GC，堆大小4086M**

```java
...
CommandLine flags: -XX:InitialHeapSize=4294967296 -XX:MaxHeapSize=4294967296 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseSerialGC 
2020-10-27T14:39:23.549+0800: 1.048: [GC (Allocation Failure) 2020-10-27T14:39:23.549+0800: 1.048: [DefNew: 1118528K->139775K(1258304K), 0.2354688 secs] 1118528K->226728K(4054528K), 0.2356788 secs] [Times: user=0.06 sys=0.17, real=0.24 secs] 
Heap
 def new generation   total 1258304K, used 585746K [0x00000006c0000000, 0x0000000715550000, 0x0000000715550000)
  eden space 1118528K,  39% used [0x00000006c0000000, 0x00000006db384a90, 0x0000000704450000)
  from space 139776K,  99% used [0x000000070ccd0000, 0x000000071554fff8, 0x0000000715550000)
  to   space 139776K,   0% used [0x0000000704450000, 0x0000000704450000, 0x000000070ccd0000)
 tenured generation   total 2796224K, used 86952K [0x0000000715550000, 0x00000007c0000000, 0x00000007c0000000)
   the space 2796224K,   3% used [0x0000000715550000, 0x000000071aa3a290, 0x000000071aa3a400, 0x00000007c0000000)
 Metaspace       used 4050K, capacity 4572K, committed 4864K, reserved 1056768K
  class space    used 454K, capacity 460K, committed 512K, reserved 1048576K
```

共发生1次GC，截取串行垃圾收集器的第一次GC，发生时间为JVM启动后的1.048秒，此时年轻代总的可用1258304KB，总的堆可用4054528KB，耗时235.68ms。

> **并行GC，堆大小4096M**

```java
...
CommandLine flags: -XX:InitialHeapSize=4294967296 -XX:MaxHeapSize=4294967296 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC 
2020-10-27T14:49:16.600+0800: 0.700: [GC (Allocation Failure) [PSYoungGen: 1048576K->174578K(1223168K)] 1048576K->253814K(4019712K), 0.0961837 secs] [Times: user=0.06 sys=0.56, real=0.10 secs] 
...
Heap
 PSYoungGen      total 1223168K, used 787478K [0x000000076ab00000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 1048576K, 58% used [0x000000076ab00000,0x0000000790185d40,0x00000007aab00000)
  from space 174592K, 99% used [0x00000007b5580000,0x00000007bffffc98,0x00000007c0000000)
  to   space 174592K, 0% used [0x00000007aab00000,0x00000007aab00000,0x00000007b5580000)
 ParOldGen       total 2796544K, used 191944K [0x00000006c0000000, 0x000000076ab00000, 0x000000076ab00000)
  object space 2796544K, 6% used [0x00000006c0000000,0x00000006cbb72320,0x000000076ab00000)
 Metaspace       used 4047K, capacity 4572K, committed 4864K, reserved 1056768K
  class space    used 455K, capacity 460K, committed 512K, reserved 1048576K
```

共发生2次GC，截取并行垃圾收集器的第一次GC，发生时间为JVM启动后的0.7秒，此时年轻代总的可用1223168KB，总的堆可用4019712KB，耗时96.18ms。

> **CMS GC，堆大小4096M**

```java
...
CommandLine flags: -XX:InitialHeapSize=4294967296 -XX:MaxHeapSize=4294967296 -XX:MaxNewSize=697933824 -XX:MaxTenuringThreshold=6 -XX:NewSize=697933824 -XX:OldPLABSize=16 -XX:OldSize=1395867648 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:-UseLargePagesIndividualAllocation -XX:+UseParNewGC 
2020-10-27T14:50:31.455+0800: 0.771: [GC (Allocation Failure) 2020-10-27T14:50:31.456+0800: 0.771: [ParNew: 545344K->68096K(613440K), 0.0940514 secs] 545344K->168157K(4126208K), 0.0943040 secs] [Times: user=0.11 sys=0.50, real=0.10 secs] 
...
Heap
 par new generation   total 613440K, used 84399K [0x00000006c0000000, 0x00000006e9990000, 0x00000006e9990000)
  eden space 545344K,   2% used [0x00000006c0000000, 0x00000006c0febe88, 0x00000006e1490000)
  from space 68096K, 100% used [0x00000006e1490000, 0x00000006e5710000, 0x00000006e5710000)
  to   space 68096K,   0% used [0x00000006e5710000, 0x00000006e5710000, 0x00000006e9990000)
 concurrent mark-sweep generation total 3512768K, used 502319K [0x00000006e9990000, 0x00000007c0000000, 0x00000007c0000000)
 Metaspace       used 4052K, capacity 4572K, committed 4864K, reserved 1056768K
  class space    used 454K, capacity 460K, committed 512K, reserved 1048576K
```

4次ParNew GC，截取CMS垃圾收集器的第一次GC，第一次GC发生时间为JVM启动后的0.771秒，此时年轻代总的可用613440KB，总的堆可用4126208KB，耗时94.3ms。

> **G1 GC，堆大小4096M**

```java
...
CommandLine flags: -XX:InitialHeapSize=4294967296 -XX:MaxHeapSize=4294967296 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC -XX:-UseLargePagesIndividualAllocation 
2020-10-27T15:12:19.821+0800: 0.668: [GC pause (G1 Evacuation Pause) (young) 204M->87M(4096M), 0.0505678 secs]
...
```

10次young GC，截取G1垃圾收集器的第一次GC，第一次GC发生时间为JVM启动后的0.668秒，此时年轻代1204MB减少到87MB，总的堆可用4096M，耗时50.56ms。





**week2-01-题目2：**

使用压测工具（wrk或sb），演练gateway-server-0.0.1-SNAPSHOT.jar 示例  

**答题如下**：

```java
PS E:\Book\训练营\week2\day03> sb -u http://localhost:8088/api/hello -c 20 -N 60
Starting at 2020/10/26 23:21:33
[Press C to stop the test]
422769  (RPS: 6627.5)
---------------Finished!----------------
Finished at 2020/10/26 23:22:37 (took 00:01:03.9202427)
Status 200:    422771

RPS: 6914.2 (requests/second)
Max: 79ms
Min: 0ms
Avg: 0.1ms

  50%   below 0ms
  60%   below 0ms
  70%   below 0ms
  80%   below 0ms
  90%   below 0ms
  95%   below 0ms
  98%   below 2ms
  99%   below 3ms
99.9%   below 12ms
```

> 程序启动参数为：` java -jar -Xmx1g -Xms1g .\gateway-server-0.0.1-SNAPSHOT.jar`。依据上面的数据可得，吞吐量为6914每秒，最大延迟为79ms，最低为0ms，平均0.1ms。再次压测，分别用**jvisualvm**和**jmc**工具对压测过程中内存使用和GC情况进行监控统计。

![jvisualvm](https://github.com/Rookie45/JAVA-000/blob/main/Week_02/gateway-jvisualvm.png)

> 上述图为**jvisualvm**工具监控内存做的数据统计，每次波峰到波谷的跳变，即发生一次GC，图中总共发生27次GC。

![gateway-jmc1](https://github.com/Rookie45/JAVA-000/blob/main/Week_02/gateway-jmc1.PNG)

![gateway-jmc2](https://github.com/Rookie45/JAVA-000/blob/main/Week_02/gateway-jmc2.PNG)

![gateway-jmc3](https://github.com/Rookie45/JAVA-000/blob/main/Week_02/gateway-jmc3.PNG)

> 上述三张图为**jmc**工具监控的数据统计，利用了它的飞行器功能，统计1分钟内程序运行中发生的情况。可以简要看出整个压测过程中，堆平均使用187MB，最大使用356MB，CPU平均使用63.8%，最大使用99.6%，GC暂停时间平均2ms608us，最大3ms990us；程序运行的PC运行的线程数为4*8=32，物理内存为16GB，默认情况下，GC线程为CPU的1/4，即为8；默认heap的大小为总内存的1/4，即4GB，而由于程序启动时指定了堆大小1GB，所以统计显示的是1GB，java8默认使用的垃圾收集器为并行收集器，与统计信息相符合。

```java
PS E:\Book\训练营\week2\day03> jmap -heap 16980
Attaching to process ID 16980, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.191-b12

using thread-local object allocation.
Parallel GC with 8 thread(s)
...
```



**week2-01-题目3：**

如果自己本地有可以运行的项目，可以按照2的方式进行演练  

**答题如下**：

```java
PS E:\Book\训练营\week2\day03> sb -u http://localhost:28089/admin/test -c 20 -N 60
Starting at 2020/10/27 7:56:28
[Press C to stop the test]
163604  (RPS: 2539.8)
---------------Finished!----------------
Finished at 2020/10/27 7:57:32 (took 00:01:04.4926086)
Status 200:    163605

RPS: 2676.9 (requests/second)
Max: 136ms
Min: 0ms
Avg: 3.6ms

  50%   below 2ms
  60%   below 3ms
  70%   below 4ms
  80%   below 5ms
  90%   below 7ms
  95%   below 10ms
  98%   below 14ms
  99%   below 18ms
99.9%   below 42ms
```

程序启动参数为：` java -jar -Xms1g -Xmx1g .\newbee-mall-1.0.0-SNAPSHOT.jar`。依据上面的数据可得，吞吐量为2676每秒，最大延迟为136ms，最低为0ms，平均3.6ms。接着分别用**jvisualvm**和**jmc**工具对压测过程中内存使用和GC情况进行监控统计。

![mall-jvisualvm](https://github.com/Rookie45/JAVA-000/blob/main/Week_02/mall-jvisualvm.PNG)

> 上述图为**jvisualvm**工具监控内存做的数据统计，压测时间[8:01:36, 8:02:40]每次波峰到波谷的跳变，即发生一次GC，图中总共发生21次GC。

![mall-jmc1](https://github.com/Rookie45/JAVA-000/blob/main/Week_02/mall-jmc1.PNG)

![mall-jmc2](https://github.com/Rookie45/JAVA-000/blob/main/Week_02/mall-jmc2.PNG)

> 上述两张图为**jmc**工具监控的数据统计，统计1分钟内程序运行中发生的情况。可以简要看出整个压测过程中，堆平均使用410MB，最大使用614MB，CPU平均使用81.2%，最大使用100%，GC暂停时间平均18ms537us，最大35ms332us；初始堆大小和最大堆大小为1GB，使用的垃圾收集器为并行收集器，共发生了72次GC，这里能看到最短暂停7ms811us。



**week2-01-思考题：**

根据上述自己对于1和2的演示，写一段对于不同 GC 的总结，提交到 Github。  

**答题如下**：



**week2-02-题目1：**

运行课上的例子，以及 Netty 的例子，分析相关现象。  

**答题如下**：

```java
PS E:\Book\训练营\week2\day03> sb -u http://localhost:8801 -c 20 -N 60
Starting at 2020/10/27 8:30:09
[Press C to stop the test]
2031    (RPS: 31.6)
---------------Finished!----------------
Finished at 2020/10/27 8:31:14 (took 00:01:04.3449906)
2045    (RPS: 31.8)                     Status 200:    2045

RPS: 33.4 (requests/second)
Max: 645ms
Min: 51ms
Avg: 587.7ms

  50%   below 595ms
  60%   below 608ms
  70%   below 617ms
  80%   below 623ms
  90%   below 623ms
  95%   below 624ms
  98%   below 625ms
  99%   below 625ms
99.9%   below 626ms

PS E:\Book\训练营\week2\day03> sb -u http://localhost:8802 -c 20 -N 60
Starting at 2020/10/27 8:33:31
[Press C to stop the test]
38530   (RPS: 600.2)
---------------Finished!----------------
Finished at 2020/10/27 8:34:35 (took 00:01:04.2749597)
Status 200:    38520
Status 303:    12

RPS: 630.6 (requests/second)
Max: 83ms
Min: 14ms
Avg: 27ms

  50%   below 27ms
  60%   below 28ms
  70%   below 29ms
  80%   below 30ms
  90%   below 31ms
  95%   below 33ms
  98%   below 36ms
  99%   below 39ms
99.9%   below 58ms
    
PS E:\Book\训练营\week2\day03> sb -u http://localhost:8803 -c 20 -N 60
Starting at 2020/10/27 8:35:57
[Press C to stop the test]
39108   (RPS: 608.4)
---------------Finished!----------------
Finished at 2020/10/27 8:37:01 (took 00:01:04.3220522)
Status 200:    39101
Status 303:    7

RPS: 640.6 (requests/second)
Max: 113ms
Min: 13ms
Avg: 26.4ms

  50%   below 27ms
  60%   below 28ms
  70%   below 29ms
  80%   below 29ms
  90%   below 30ms
  95%   below 31ms
  98%   below 34ms
  99%   below 38ms
99.9%   below 72ms

PS E:\Book\训练营\week2\day03> sb -u http://localhost:8808/test -c 20 -N 60
Starting at 2020/10/27 8:40:28
[Press C to stop the test]
372229  (RPS: 5797.8)
---------------Finished!----------------
Finished at 2020/10/27 8:41:32 (took 00:01:04.4027045)
Status 200:    372238

RPS: 6081.2 (requests/second)
Max: 98ms
Min: 0ms
Avg: 0ms

  50%   below 0ms
  60%   below 0ms
  70%   below 0ms
  80%   below 0ms
  90%   below 0ms
  95%   below 0ms
  98%   below 0ms
  99%   below 2ms
99.9%   below 3ms
```
`HttpServer01`在每次请求来时，都是main线程处理，也就导致所有请求串行处理，所以四种实现中，压测吞吐量最低。`HttpServer02`在每次请求来时，都new一个thread处理，压测吞吐量630.6 每秒，但这非常消耗线程资源，且线程的创建和销毁是非常消耗时间。`HttpServer03`在每次请求来时，都交给创建的线程池处理，压测吞吐量640.6每秒，这里线程资源可控。到了`NettyServerApplication`，吞吐量直接涨了一个数量级，压测是6081.2每秒，而且平均延迟也是最低0ms，Netty将原先的同步改为了异步，基于事件驱动，最大程度上接受请求。


**week2-02-题目2：**

写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801，代码提交到
Github  

**答题如下：**

HttpClient的实现：
[CustomHttpClient.java](https://github.com/Rookie45/JAVA-000/blob/main/Week_02/CustomHttpClient.java)

OkHttp的实现：
[CustomOkHttp.java](https://github.com/Rookie45/JAVA-000/blob/main/Week_02/CustomOkHttp.java)


参考

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)

[如何选择合适的 GC 时间 —— USER, SYS, REAL？](https://cloud.tencent.com/developer/article/1491229)

[HttpClient、OkHttp、RestTemplate、WebClient的基本使用](https://www.jianshu.com/p/afc96b7de90c)


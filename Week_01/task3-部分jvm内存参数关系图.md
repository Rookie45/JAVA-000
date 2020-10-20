**题目：**

画一张图，展示 Xmx、Xms、Xmn、Meta、DirectMemory、Xss 这些内存参数的关系  。

**答题如下：**

![JVM参数之间关系.png](https://github.com/Rookie45/JAVA-000/blob/main/Week_01/JVM%E5%8F%82%E6%95%B0%E4%B9%8B%E9%97%B4%E5%85%B3%E7%B3%BB.png)

> Xmx：最大堆大小；
> Xms：最小堆大小；
> Xmn：年轻代大小；
> Xss：每个线程的栈大小；
> Meta：元空间；
> DirectMemory：直接内存

> Xss如果不显示设置，在64位Linux上HotSpot的线程栈容量默认是1MB，此外内核数据结构还会额外消耗16KB。而一个协程的栈通常在几百字节到几KB之间，差了1个数量级。

> G1 gc一般推荐Xms==Xmx，同时不设置Xmn，这是由于G1适合由自己完全管理内存，Young区可以根据需要控制有多少个region，如果设置了Xmn，反而定死了Young区。Xms和Xmx设置时，可以配置-XX:+HeapDumpOnOutOfMemoryError参数，让虚拟机在出现内存溢出时Dump出当前的内存堆转储快照，以便事后分析。那推荐-XX:+HeapDumpOnOutOfMemoryError在线上环境配置吗？另外关于Xms，经验值一般设置为当前系统内存的70%。

> metaspace代替了永久代，存储一些类型信息，比如静态变量和方法，与Compressed Class Space有交叉

> DirectMemory属于堆外空间，容量大小通过-XX:MaxDirectMemorySize指定，如果不指定，默认与堆（-Xmx的值）一致。

> 非堆内存与堆外内存的区别：非堆内存依然归JVM管理，但业务代码无法涉及到，堆外内存一般是为了绕开GC直接使用的内存，业务代码特别是JNI用，通常情况下可能是非Java语言或者DirectBuffer使用。

参考：

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)

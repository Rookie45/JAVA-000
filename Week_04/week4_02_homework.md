**题目：**

1、列举常用的并发操作API和工具类，简单分析其使用场景和优缺点。

**答题如下：**


| API和工具类 | 使用场景 | 优点 | 缺点 |
|  ----  | ----  |----  |----  |
| Lock/Condition | 支持中断的场景 |1.能够响应中断；2.支持超时；3.非阻塞获取锁 |显示加锁和解锁 |
| LockSupport |  | |单元格 |
| AtomicXxx原子类 | 计数器 | 无锁                                       |单元格 |
| Semaphore | 单元格 |单元格 |单元格 |
| CountdownLatch | 单元格 |单元格 |单元格 |
| CyclicBarrier | 单元格 |单元格 |单元格 |
| Future | 单元格 |单元格 |单元格 |
| FutureTask | 单元格 |单元格 |单元格 |
| CompletableFuture | 单元格 |单元格 |单元格 |
| ReadWriteLock | 单元格 |单元格 |单元格 |
| StampedLock | 单元格 |单元格 |单元格 |
| CopyOnWriteArrayList | 单元格 |单元格 |单元格 |
| ConcurrentHashMap | 单元格 |单元格 |单元格 |

**题目：**

2、请思考：什么是并发？什么是高并发？实现高并发高可用系统需要考虑哪些因素，对于这些你是怎么理解的？

**答题如下：**

并发（concurrent），指多个线程在共同完成一件事情; 互相之间有依赖/有状态，例如多个部门做同一个系统。

高并发，指

高并发高可用考虑因素：



**题目：**

3、请思考：还有哪些跟并发类似/有关的场景和问题，有哪些可以借鉴的解决办法。

**答题如下：**

数据库事务



**题目：**

4、把多线程和并发相关知识带你梳理一遍，画一个脑图，截图上传到github上。

**答题如下：**



参考

[java进阶训练营](https://u.geekbang.org/subject/java/1000579?utm_source=u_list_web&utm_medium=u_list_web&utm_term=u_list_web)
